function initMap() {
    const uluru = { lat: 10.820084005694037, lng: 106.69064060863472 };
    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 12,
        center: uluru,
        mapTypeId: "roadmap",
    });

    // TODO: Replace with your app's Firebase project configuration
    var firebaseConfig = {
        apiKey: "AIzaSyCkbSYd9sBP_45ef60p5DUCnOrS8RnW2HE",
        authDomain: "shippermanager-606da.firebaseapp.com",
        databaseURL: "https://shippermanager-606da-default-rtdb.firebaseio.com",
        projectId: "shippermanager-606da",
        storageBucket: "shippermanager-606da.appspot.com",
        messagingSenderId: "460240651199",
        appId: "1:460240651199:android:fe75318b5b3d43ecb2570a",
        measurementId: "G-3G1E4LYT51"
    };

    firebase.initializeApp(firebaseConfig);

    // Get a reference to the database service
    //var database = firebase.database();

    const markers = [];

    var starCountRef = firebase.database().ref('Shipper');
    
    starCountRef.on('value', (snapshot) => {
        var i = 0;
        snapshot.forEach((object) => {
            var data = object.val();
            var location = { lat: data.KinhDo, lng: data.ViDo }
            //nếu chưa có marker thì tạo mới
            if (markers[i] == null) {
                const marker = new google.maps.Marker({
                    position: location,
                    map: map,
                    icon: {
                        labelOrigin: new google.maps.Point(28, 64),
                        url: 'http://maps.google.com/mapfiles/kml/shapes/motorcycling.png'
                    },
                    label: { color: '#000000', fontWeight: 'bold', fontSize: '14px', text: data.Ten }
                });
                ////////////////
                const contentString =
                    '<div id="content">' +
                    '<div id="siteNotice">' +
                    "</div>" +
                    '<img src="'+data.ImagePath+'" height="100" width="100" asp-append-version="true" />'+
                    '<h1 id="firstHeading" class="firstHeading">'+data.Ten+'</h1>' +
                    '<div id="bodyContent">' +
                    "<p>quê quán: "+data.QueQuan+"</p>" +
                    "<p>ngày sinh: "+data.NgaySinh+"</p>" +
                    "</div>" +
                    "</div>";
                const infowindow = new google.maps.InfoWindow({
                    content: contentString,
                });
                //add marker event
                marker.addListener("click", () => {
                    infowindow.open({
                        anchor: marker,
                        map,
                        shouldFocus: false,
                    });
                });

                //////////
                markers.push(marker);
            }
            else//nếu có thì cập nhật vị trí
            {
                markers[i].setPosition(location)
            }
            i++;
        })

    });

}

