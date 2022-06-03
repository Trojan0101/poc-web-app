//Marker Cluster
function initMap(callback) {
    const map = new google.maps.Map(document.getElementById("map"), {
        zoom: 3,
        center: {
            lat: -28.024,
            lng: 140.887
        },
    });
    const infoWindow = new google.maps.InfoWindow({
        content: "",
        disableAutoPan: true,
    });
    // Create an array of alphabetical characters used to label the markers.
    const labels = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    // Add some markers to the map.
    const markers = locations.map((position, i) => {
        const label = labels[i % labels.length];
        const marker = new google.maps.Marker({
            position,
            label,
        });

        // markers can only be keyboard focusable when they have click listeners
        // open info window when marker is clicked
        marker.addListener("click", (mapsMouseEvent) => {

            const clickedMarkerPositionLat = mapsMouseEvent.latLng.toJSON().lat;
            const clickedMarkerPositionLng = mapsMouseEvent.latLng.toJSON().lng;

            const commentsNode = document.createElement('div');
            const listNode = document.createElement('ul');

            let commentsData = fetch("/showComments");
            commentsData.then(response =>
                response.json()).then(comments => {
                    console.log(comments);
                for (let i = 0; i < comments.length; i++) {
                    if (comments[i]["latitude"] == clickedMarkerPositionLat &&
                        comments[i]["longitude"] == clickedMarkerPositionLng) {
                        const userFirstName = comments[i]["firstName"];
                        const userComment = comments[i]["comment"];
                        const userPicture = comments[i]["picturename"];
                        const li = document.createElement('li');

                        const img = document.createElement("img", innerWidth = 5, innerHeight = 5);
                        const pictureNode = document.createElement('div');

                        let pictureData = fetch("/getCommentPicture?filename=" + userPicture);
                        pictureData.then(response =>
                            response.blob())
                            .then(picture => {
                                const pictureURL = URL.createObjectURL(picture);
                                img.src = pictureURL;
                                pictureNode.append(img);
                            })

                        const firstNameTag = document.createElement('a');
                        const commentTag = document.createElement('p');
                        firstNameTag.append(userFirstName);
                        li.append(firstNameTag);
                        li.append(pictureNode);
                        commentTag.append(userComment);
                        li.append(commentTag);
                        listNode.appendChild(li);

                        // li.append(pictureNode);
                        // li.appendChild(document.createTextNode(userEmail + ':<br> ' + userComment));
                        // listNode.appendChild(li);
                    }
                }
            })
            commentsNode.appendChild(listNode);


            // Add the logic to show a popup window with add-comments button
            const formContent =
                '<form name="commentsForm" method="POST" enctype="multipart/form-data" action="/addComments">' +
                '<input type="hidden" id="latitude" name="latitude" value=' + clickedMarkerPositionLat + '>' +
                '<input type="hidden" id="longitude" name="longitude" value=' + clickedMarkerPositionLng + '>' +
                '<textarea id="commentTextBox" name="comment"></textarea><br>' +
                '<input type="file" id="commentImage" name="file"><br>' +
                '<button type = "submit" id="addCommentButton" class="btn btn-primary" value="Upload">Add Comment</button>' +
                '</form>';

            const infoWindowNode = document.createElement('div');
            const formNode = document.createElement('div');
            formNode.innerHTML = formContent;
            infoWindowNode.appendChild(commentsNode);
            infoWindowNode.appendChild(formNode);

            infoWindow.setContent(
                infoWindowNode
            );
            infoWindow.open(map, marker);
        });
        return marker;
    });

    // Add a marker clusterer to manage the markers.
    new markerClusterer.MarkerClusterer({
        map,
        markers
    });
}

const locations = [];
let usersLocation = String(message);
usersLocation = usersLocation.substring(1, usersLocation.length - 1).replaceAll("], [", "]:[");
const usersLocationArray = usersLocation.split(":");

for (let i = 0; i < usersLocationArray.length; i++) {
    let newArray = usersLocationArray[i].replaceAll("[", "").replaceAll("]", "").split(",");
    const location = {
        lat: Number(newArray[0]),
        lng: Number(newArray[1])
    };
    locations.push(location);
}

window.initMap = initMap;