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

            const clickedMarkerPosition =
                JSON.stringify(mapsMouseEvent.latLng.toJSON(), null, 2);

            // Add the logic to show a popup window with add-comments button
            const formContent =
                '<form method="POST" enctype="multipart/form-data" action="/addComments">' +
                '<input type="hidden" id="latlng" name="latlng">' +
                '<textarea id="commentTextBox" name="comment"></textarea><br>' +
                '<input type="file" id="commentImage" name="file"><br>' +
                '<button type = "submit" id="addCommentButton" class="btn btn-primary" value="Upload">Add Comment</button>' +
                '</form>';

            const infoWindowNode = document.createElement('div');
            const formNode = document.createElement('div');
            formNode.innerHTML = formContent;
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

function addComments() {
    alert("Add sample comments!");
}

window.initMap = initMap;