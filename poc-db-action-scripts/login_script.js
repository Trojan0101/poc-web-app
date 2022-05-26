function login(email, password, callback) {
  //this example uses the "pg" library
  //more info here: https://github.com/brianc/node-postgres

  const bcrypt = require('bcrypt');
  const postgres = require('pg');

  const conString = configuration.POSTGRES_URL;
  postgres.connect(conString, function (err, client, done) {
    if (err) return callback(err);

    const query = 'SELECT id, email, password FROM users WHERE email = $1';
    client.query(query, [email], function (err, result) {
      // NOTE: always call `done()` here to close
      // the connection to the database
      done();

      if (err || result.rows.length === 0) return callback(err || new WrongUsernameOrPasswordError(email));

      const user = result.rows[0];

      bcrypt.compare(password, user.password, function (err, isValid) {
        if (err || !isValid) return callback(err || new WrongUsernameOrPasswordError(email));
        
        //EXTRA TO ADD LOGIN DETAILS
		const validateQuery = 'SELECT email FROM login_details WHERE email = $1';
        client.query(validateQuery, [email], function (err, result) {
      		done();
          
          if (result.rows.length === 0) {
            const query = 'INSERT INTO login_details(email, is_logged_in) VALUES ($1, $2)';
      			client.query(query, [user.email, true], function (err, result) {
        			done();
        		});
          } else {
            const query = 'UPDATE login_details set is_logged_in=$1 where email=$2';
      			client.query(query, [true, user.email], function (err, result) {
        			done();
        		});
          }
        });
        //EXTRA TO ADD LOGIN DETAILS
       
        return callback(null, {
          user_id: user.id,
          email: user.email
        });
      });
    });
  });
}
