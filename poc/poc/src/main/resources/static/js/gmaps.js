//Marker Cluster
function initMap(callback) {
    let latitude = parseFloat(sessionStorage.getItem("latitude"));
    let longitude = parseFloat(sessionStorage.getItem("longitude"));
    let mapZoom = parseInt(sessionStorage.getItem("mapZoom"));

    if (latitude == undefined || isNaN(latitude) || longitude == undefined || isNaN(longitude)) {
        latitude = -28.024;
        longitude = 140.887;
        mapZoom = 3;
    }

    const map = new google.maps.Map(document.getElementById("map"), {
        zoom: mapZoom,
        center: {
            lat: latitude,
            lng: longitude
        },
    });
    sessionStorage.clear();
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

                        const beforeSpanTag = document.createElement('span');
                        const afterSpanTag = document.createElement('span');
                        beforeSpanTag.setAttribute('id','dots');
                        afterSpanTag.setAttribute('id','more');

                        const readMore = document.createElement('button');
                        readMore.setAttribute('onclick', 'readMoreButton(this)');
                        readMore.setAttribute('id', 'readMoreButton');
                        readMore.innerHTML = "Read more";

                        firstNameTag.append(userFirstName);
                        li.append(firstNameTag);
                        const lineBreak1 = document.createElement('p');
                        li.append(lineBreak1);
                        li.append(pictureNode);
                        const lineBreak2 = document.createElement('p');
                        li.append(lineBreak2);

                        commentTag.append(userComment.toString().substring(0, 20));
                        beforeSpanTag.append('...');
                        afterSpanTag.append(userComment.toString().substring(20));
                        commentTag.append(beforeSpanTag);
                        commentTag.append(afterSpanTag);
                        commentTag.append(readMore);
                        const separatorLine = document.createElement('hr');
                        separatorLine.setAttribute('class', 'solid');
                        li.append(commentTag);
                        li.append(separatorLine);
                        listNode.appendChild(li);
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

function readMoreButton(btn) {
    let parent = btn.parentElement;
    const dots = parent.firstElementChild;
    const moreText = parent.firstElementChild.nextElementSibling;
    const btnText = parent.firstElementChild.nextElementSibling.nextElementSibling;

    if (dots.style.display === "none") {
        dots.style.display = "inline";
        btnText.innerHTML = "Read more";
        moreText.style.display = "none";
    } else {
        dots.style.display = "none";
        btnText.innerHTML = "Read less";
        moreText.style.display = "inline";
    }
}

function focusLocation(comment) {
    let commentData = fetch("/getLatLng?comment=" + comment.innerHTML.toString());
    commentData.then(response =>
        response.json()).then(comments => {
        sessionStorage.setItem("latitude", comments["latitude"]);
        sessionStorage.setItem("longitude", comments["longitude"]);
        sessionStorage.setItem("mapZoom", "25");
    });
    location.replace("/dashboard");
}

window.initMap = initMap;