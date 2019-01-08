<html>
  <head>
    <style>
      /* Set the size of the div element that contains the map */
      #map {
        height: 400px;  /* The height is 400 pixels */
        width: 100%;  /* The width is the width of the web page */
       }
	   .container{
	   	margin:0 auto;
	   	width:1200px
	   }
	   .eventForm{
	   	width:575px;
	   	height: 195px;
	   	margin:0 auto;
	   	border: 5px solid grey;
	   	padding:0 10px;
	   	margin-bottom: 20px;
	   }
	   .eventForm h1{
	   	text-align:center;
	   	font-family:serif;
	   	margin:0
	   }
	   .eventForm div{
	   	float:left
	   }
	   table{
	   	margin:0 auto;
	   }
	   .notFound{
	   	text-align: center;
	   }
	   .notFound p{
	   	    background: lightgrey;
    		border: 2px solid grey;
	   }
/*	   #ticketLink*/
	   a{
	   	text-decoration: none;
	   	color: black;
	   }
	   #map{
	   	width: 600px;
	   	height: 400px
	   }
	   button{
	   	display: block;
	   	border: none;
	   	background: lightgrey;
	   	width: 80px;
	   	padding: 3px 0;
	   	outline: none;
	   }
	   button:hover{
        	background: grey;
        	border:none;
        	color: black;
        }
	   #picture table{
	   	text-align: center;
	   }
	   .popup{
	   	position: relative;
	   }
	   .popup .popupmap{
	   	position: absolute;
	   	visibility: hidden;
	   	width: 400px;
	   	height: 400px;
	   	z-index: 1;
	   }
	   .popup .visible{
	   	visibility: visible
	   }
	   #venueShow, #pictureShow{
	   	text-align:center;
	   }
	   #venue, #picture{
	   	display: none;
	   }

 	span{
    	display:inline-block;
    	width: 30px;
    	height: 30px;
    }
    .border{
    	border-top: 6px solid lightgrey;
    	border-right: 6px solid lightgrey;
    }
    .up{
    	transform: rotate(-45deg);
    	margin-top: 14px;
    	margin-right: 14px;
    }
    .up span{
    	transform: matrix(1,0.3,0.3,1,4,-5);
    }
    .down{
    	transform: rotate(135deg);
    }
    .down span{
    	transform: matrix(1,0.3,0.3,1,-5,1);
    }

    </style>
  </head>
  <body>
  
  	<?php
  		include 'geoHash.php';
		
		function getJSON($url){
			return json_decode(file_get_contents($url),true);
		}

  	?>
  	<?php 
  		session_start();
  		if(isset($_POST['submit'])){
  			$_SESSION['keyword'] = $_POST['keyword'];
  			$_SESSION['category'] = $_POST['category'];
  			$_SESSION['distance'] = $_POST['distance'];
  			$_SESSION['location'] = $_POST['location'];
  			$_SESSION['lat'] = $_POST['lat'];
  			$_SESSION['lon'] = $_POST['lon'];

  			if(isset($_POST['otherLocation']))
  				$_SESSION['otherLocation'] = $_POST['otherLocation'];

 			$lat = $_POST['lat'];
			$lon = $_POST['lon'];			
			if($_POST['location'] == 'other'){
				$locURL = "https://maps.googleapis.com/maps/api/geocode/json?key=".$GOOGLE_API_KEY."&address=".urlencode($_POST["otherLocation"]);
				//echo $locURL;
				$locJSON = getJSON($locURL);
				$lat = $locJSON['results'][0]['geometry']['location']['lat'];
				$lon = $locJSON['results'][0]['geometry']['location']['lng'];
				$_SESSION['lat'] = $lat;
				$_SESSION['lon'] = $lon;
				//echo $lat."   ".$lon;
				//var_dump($locJSON);
			}
			//echo $lat."   ".$lon;
			$ticket_url = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=".$TICKET_API_KEY."&keyword=".urlencode($_POST["keyword"])."&segmentId=".$_POST["category"]."&radius=".$_POST["distance"]."&unit=miles&geoPoint=".encode($lat,$lon);
			//echo $ticket_url;
			$ticketJSON = file_get_contents($ticket_url); 
			
			//var_dump($ticketJSON);

  		}
  		if(isset($_GET['eventId'])){
  			$_SESSION['eventId'] = $_GET['eventId'];
  			$eventURL = "https://app.ticketmaster.com/discovery/v2/events/".$_SESSION['eventId']."?apikey=".$TICKET_API_KEY;
  			$eventJSON = file_get_contents($eventURL);
  			$eventJSON_arr = json_decode($eventJSON,true);
  			if(isset($eventJSON_arr['_embedded']['venues'])){
				$venue_url = "https://app.ticketmaster.com/discovery/v2/venues?apikey=".$TICKET_API_KEY."&keyword=".urlencode($eventJSON_arr['_embedded']['venues'][0][name]);
				//echo $venue_url;
				$venueJSON = file_get_contents($venue_url);
				// $venueJSON_arr = json_decode($venueJSON,true);
				// $_SESSION['venue_lat'] = $venueJSON_arr['_embedded']['venues'][0]['location']['latitude'];
				// $_SESSION['venue_lon'] = $venueJSON_arr['_embedded']['venues'][0]['location']['longitude'];
			}


  		}
  		if(isset($_POST['clear'])){
  			session_destroy();
  			unset($_SESSION);
  		}
  	?>
  	
	<div class = "container">
		<div class ="eventForm">
			<h1><i>Event Search</i></h1>
			<hr />
			<form method="POST" action="event.php">
				<div>
					<label for="keyword">Keyword</label><input type="text" name="keyword" value="<?php echo $_SESSION["keyword"]; ?>"required></br>
					<label for="category">Category</label><select name="category">
						<option value="">default</option>
						<option value="KZFzniwnSyZfZ7v7nJ" <?php echo isset($_SESSION["category"])&& $_SESSION["category"] == "KZFzniwnSyZfZ7v7nJ"?"selected":"" ?>>Music</option>
						<option value="KZFzniwnSyZfZ7v7nE" <?php echo isset($_SESSION["category"])&& $_SESSION["category"] == "KZFzniwnSyZfZ7v7nE"?"selected":"" ?>>Sports</option>
						<option value="KZFzniwnSyZfZ7v7na" <?php echo isset($_SESSION["category"])&& $_SESSION["category"] == "KZFzniwnSyZfZ7v7na"?"selected":"" ?>>Art & Theatre</option>
						<option value="KZFzniwnSyZfZ7v7nn" <?php echo isset($_SESSION["category"])&& $_SESSION["category"] == "KZFzniwnSyZfZ7v7nn"?"selected":"" ?>>Film</option>
						<option value="KZFzniwnSyZfZ7v7n1" <?php echo isset($_SESSION["category"])&& $_SESSION["category"] == "KZFzniwnSyZfZ7v7n1"?"selected":"" ?>>Miscellaneous</option>
					</select></br>
					<label for="distance">Distance(miles)</label><input type="number" name="distance" placeholder="distance" value="<?php echo isset($_POST['distance'])?$_POST['distance']:10 ?>"><label for="location">from</label>
				</div>
				
				<div style="margin-top: 45px;">
					<input type="radio" id="local" name="location" value="local" onClick="changeRequired()" checked><label for="local">Here</label></br>
					<input type="radio" id="other" name="location" value="other" onClick="changeRequired()" <?php echo isset($_SESSION["location"])&& $_SESSION["location"] == "other"?"checked":""?>>
					<input type="text" name="otherLocation" id="otherLocation" value="<?php echo $_SESSION['otherLocation'] ?>" placeholder="location" <?php echo isset($_SESSION['location'])&&$_SESSION['location']=='other'?"":"disabled" ?>>
				</div>
				
				<div style="width: 200px;margin-left: 65px;margin-top: 10px;">
					<input type="submit" name="submit" id ="submit" value="Search" disabled><input type="submit" name="clear" value="Clear">
				</div>
				
				<input type="hidden" id="lat" name="lat"><input type="hidden" id="lon" name="lon">
				
			</form>
		</div>
		

		<div id = "show"></div>
		<div id = "notFound"></div>
		<div id = "venueShow"></div>
		<div id="venue"></div>
		<div id="pictureShow"></div>
		<div id="picture"></div>

<!-- 		<?php
			if(isset($_POST['submit'])){
				foreach($_POST as $key=>$val){
					echo "$key: $val</br>";
				}
			}
		?> -->

	</div>
	<script 
    src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCk4BpaxE_kQc8V4BGt_NA8UTe8wojz8gI">
    </script>
	<script>
		window.onload = getLocalPosition();
		<?php
			if(isset($ticketJSON)){
				echo "var jsonObj = $ticketJSON;";
				echo "generateHTML($ticketJSON);";
			}
			if(isset($eventJSON)){
				echo "var eventJSON = $eventJSON;";
				echo "generateDetails(eventJSON);";
			}
			if(isset($venueJSON)){
				echo "var venueJSON = $venueJSON;";
				echo "generateVenue(venueJSON);";
			}
			if(isset($_SESSION['lat'])){
				echo "from = new google.maps.LatLng(Number(".$_SESSION['lat']."),Number(".$_SESSION['lon']."));";
			}
		?>
		
		function changeRequired(){
			var loc = document.getElementById("otherLocation");
			if(document.getElementById("other").checked){
				loc.required=true;
				loc.disabled=false;
			}else{
				loc.required=false;
				loc.disabled=true;
			}
		}
		function getLocalPosition(){
			function loadJSON(url){
				request = new XMLHttpRequest();
				request.open('GET',url,false);
				request.send();
				jsonObj = JSON.parse(request.responseText);
				return jsonObj;
			}
			locJson = loadJSON("http://ip-api.com/json");
			//var locJson = JSON.parse(<?php echo "'".file_get_contents("http://ip-api.com/json")."'" ?>);
			//console.log(locJson);
			document.getElementById("lat").value = locJson.lat;
			document.getElementById("lon").value = locJson.lon;
			document.getElementById("submit").disabled = false;			
		}
		
		function generateHTML(jsonObj){
			var html_text = "";
			var eventTable = document.getElementById("show");
			console.log(jsonObj);
			if(jsonObj._embedded!=null){
				var events = jsonObj._embedded.events;
				//console.log(root);
				html_text += "<table border='2'><tbody><tr><th>Date</th><th>Icon</th><th width='400px'>Event</th><th width='70px'>Genre</th><th width='400px'>Venue</th></tr>";
				events.forEach(function(event){
					html_text+="<tr><td style='text-align:center;'>";
					if(!event.dates.start.dateTBA){
						 html_text+= event.dates.start.localDate;
					}
					if(event.dates.start.localTime != null){
						html_text+= "</br>"+event.dates.start.localTime;
					}
					html_text += "</td><td><img src='" + event.images[0].url + "' width = '100'></td>";
					html_text += "<td><a href='event.php?eventId="+event.id+"'>" + event.name + "</a></td>";

					html_text += "<td>";
					if(event.classifications!=null){
						html_text += event.classifications[0].segment.name;
					}
					html_text  += "</td>";

					html_text += "<td><div onClick='popup(this.nextElementSibling.childNodes);'>" + event._embedded.venues[0].name + "</div><div class='popup'><div class='popupmap' id='myPopUp'></div>" + 
					"<input type='hidden' name='lat' value="+ event._embedded.venues[0].location.latitude +"><input type='hidden' name='lon' value="+ event._embedded.venues[0].location.longitude +">" + 
					"<div class='popupmap' id='panel'><button value='WALKING' onClick='calcRoute(this.value)'>Walk there</button><button value='BICYCLING' onClick='calcRoute(this.value)'>Bike there</button><button value='DRIVING' onClick='calcRoute(this.value)'>Drive there</button></div>"
					+"</div></td></tr>";
				});
				html_text += "</tbody></table>"
				eventTable.innerHTML = html_text;
			}else{
				html_text += "<p>No Reccords has been found</p>"
				document.getElementById("show").innerHTML=html_text;
				document.getElementById("show").classList.add("notFound");
				console.log("no events!");
			}
		}

		function generateDetails(eventObj){
			console.log(eventObj);
			var html_text = "";
			html_text += "<h2 style='text-align:center;'>" + eventObj.name +"</h2>";
			// html_text += "<div style='float:left;'>";
			html_text += "<table><tbody><tr><td>";
			if(eventObj.dates.start.localDate != null){
				html_text += "<h3>Date</h3><p>" + eventObj.dates.start.localDate;
				if(eventObj.dates.start.localTime!=null)
					html_text += " "+eventObj.dates.start.localTime;
				html_text += "</p>"
			}

			var team = eventObj._embedded.attractions;
			if(team != null){
				html_text += "<h3>Artist/Team</h3><p>";				
				for(var i=0;i<team.length;i++){
				 	html_text += "<a href='" + team[i].url + "' target='_blank'>" + team[i].name + "</a>";
					if(i != team.length-1)
						html_text += " | ";
				}
				html_text += "</p>";
			}

			var venues = eventObj._embedded.venues;
			if(venues != null){
				html_text += "<h3>Venue</h3><p>";				
				for(var i=0;i<venues.length;i++){
					if(i == venues.length-1)
						html_text += venues[i].name;
					else{
						html_text += venues[i].name + " | ";
					}
				}
				html_text += "</p>"			
			}

			if(eventObj.classifications != null){
				html_text += "<h3>Genre</h3><p>";
				var genres = eventObj.classifications[0];
				var genreText = "";
				if(genres.subGenre!=null)
					genreText += genres.subGenre.name + " | ";
				if(genres.genre!=null)
					genreText += genres.genre.name + " | ";				
				if(genres.segment!=null)
					genreText += genres.segment.name + " | ";
				if(genres.subType!=null)
					genreText += genres.subType.name + " | ";
				if(genres.type!=null)
					genreText += genres.type.name + " | ";
				genreText = genreText.slice(0,-2);
				html_text +=genreText + "</p>";
			}

			if(eventObj.priceRanges!=null){
				var price = eventObj.priceRanges[0];
				html_text += "<h3>Price Ranges</h3>";
				html_text += "<p>" + price.min + " - " + price.max + " " + price.currency + "</p>";
			}
			
			if(eventObj.dates!=null && eventObj.dates.status!=null){
				html_text+="<h3>Ticket Status</h3><p>" + eventObj.dates.status.code + "</p>";
			}

			var url = eventObj.url;
			if(url!=null){
				html_text += "<h3>Buy Ticket At</h3><p><a id='ticketLink' href='" + url + "' target='_blank'>Ticketmaster</a></p>";
			}
			html_text += "</td>";

			if(eventObj.seatmap!=null){
				html_text += "<td><img src='" + eventObj.seatmap.staticUrl +"'></td>";
			}
			html_text += "</tr></tbody></table>"
			// html_text += "</div><div style='float:left;'>"
			// html_text += "</div>";
			document.getElementById("show").innerHTML=html_text;
		}

		function generateVenue(venueJSON){
			console.log(venueJSON);
			var html_text = "";
			document.getElementById("venueShow").innerHTML = "<p style='margin-bottom:0;color:lightgrey;'>click to show venue info</p><div id='arrow1' onClick='toggleInfo1(this)'><span class='down'><span class='border'></span></span></div>";
			if(venueJSON._embedded!=null && venueJSON._embedded.venues!=null){
				html_text += "<table border='1'><tbody><tr><th>Name</th><td>";
				var venue = venueJSON._embedded.venues[0];
				if(venue.name!=null)
					html_text += venue.name;
				else
					html_text += "N/A";
				html_text += "</td></tr><tr><th>Map</th><td>";

				html_text += "<div style='float:left'><button value='WALKING' onClick='calcRoute(this.value)'>Walk there</button><button value='BICYCLING' onClick='calcRoute(this.value)'>Bike there</button><button value='DRIVING' onClick='calcRoute(this.value)'>Drive there</button></div><div id='map' style='float:left;'></div>"

				html_text += "</td></tr><tr><th>Address</th><td>"
				if(venue.address!=null)
					html_text += venue.address.line1;
				else
					html_text += "N/A";

				html_text += "</td></tr><tr><th>city</th><td>";
				if(venue.city!=null){
					html_text += venue.city.name + ", " + venue.state.stateCode;
				}else{
					html_text += "N/A";
				}

				html_text += "</td></tr><tr><th>Postal Code</th><td>";
				if(venue.postalCode!=null)
					html_text += venue.postalCode;
				else
					html_text += "N/A";

				html_text += "</td></tr><tr><th>Upcoming Events</th><td>";
				if(venue.url!=null)
					html_text += "<a href='" + venue.url + "' id='ticketLink' target='_blank'>" + venue.name + " Tickets</a>"
				else
					html_text += "N/A";

				html_text += "</td></tr></tbody></table>";

				document.getElementById("venue").innerHTML = html_text;

				console.log(venue.location);
				if(venue.location!=null){
					var el = document.getElementById('map');
					console.log(el);
					initMap(Number(venue.location.latitude),Number(venue.location.longitude),el);
					//to = new google.maps.LatLng(Number(venue.location.latitude),Number(venue.location.longitude));	
				}
		
			}else{
				html_text += "<p>No Venue Info Found</p>";
				document.getElementById("venue").classList.add("notFound");
				document.getElementById("venue").innerHTML = html_text;
			}



			var html_img = "";
			document.getElementById("pictureShow").innerHTML = "<p style='margin-bottom:0;color:lightgrey;'>click to show venue photos</p><div id='arrow2' onClick='toggleInfo2(this)'><span class='down'><span class='border'></span></span></div>";
			if(venueJSON._embedded!=null && venueJSON._embedded.venues!=null && venueJSON._embedded.venues[0].images!=null){
				html_img += "<table border='1'><tbody>";
				venueJSON._embedded.venues[0].images.forEach(function(image){
					html_img += "<tr><td><img src='" + image.url + "' " 
					if(image.width>1100)
						html_img += "width='1100px'";
					html_img += "></td></tr>";
				});
				html_img += "</tbody></table>";
			}else{
				html_img += "<p>No Venue Photos Found</p>";
				document.getElementById("picture").classList.add("notFound");
			}

			document.getElementById("picture").innerHTML = html_img;
		}

		function initMap(latitude, longitude, element){
			  //if(node==null)	return;
			  directionsService = new google.maps.DirectionsService();
  			  directionsDisplay = new google.maps.DirectionsRenderer();
			  // The location of Uluru
			  var uluru = {lat: latitude, lng: longitude};
			  // The map, centered at Uluru
			  var map = new google.maps.Map(
			      element, {zoom: 14, center: uluru});
			  // The marker, positioned at Uluru
			  marker = new google.maps.Marker({position: uluru, map: map});
			  directionsDisplay.setMap(map);
			  to = new google.maps.LatLng(latitude,longitude);
		}

		function calcRoute(mode){
			console.log(mode);
			var request = {
				origin:from,
				destination:to,
				travelMode:mode
			};
			directionsService.route(request, function(response, status) {
			    if (status == 'OK') {
			      directionsDisplay.setDirections(response);
			    }
			});
			marker.setVisible(false);
			// var request = {
			// 	origin:
			// }
		}
		function popup(node){
			console.log(node);
			initMap(Number(node[1].value),Number(node[2].value), node[0]);
			node[0].classList.toggle('visible');
			node[3].classList.toggle('visible');
			//initMap(,node)
			//document.getElementById('myPopUp').classList.toggle('visible');
		}
		function toggleInfo1(arrow){
			var venue = document.getElementById('venue');
			if(arrow.firstChild.className == 'down'){
				arrow.firstChild.classList.replace('down','up');
				arrow.previousElementSibling.firstChild.nodeValue = "click to hide venue info";
				venue.style.display = 'block';
				if(document.getElementById("picture").style.display == 'block')
					toggleInfo2(document.getElementById('arrow2'));
	
			}else{
				arrow.firstChild.classList.replace('up','down');
				arrow.previousElementSibling.firstChild.nodeValue = "click to show venue info";
				venue.style.display = 'none';
			}
			
		}

		function toggleInfo2(arrow){
			var picture = document.getElementById('picture');
			if(arrow.firstChild.className == 'down'){
				arrow.firstChild.classList.replace('down','up');
				arrow.previousElementSibling.firstChild.nodeValue = "click to hide venue photos";
				picture.style.display = 'block';
				if(document.getElementById("venue").style.display=='block')
					toggleInfo1(document.getElementById('arrow1'));
			}else{
				arrow.firstChild.classList.replace('up','down');
				arrow.previousElementSibling.firstChild.nodeValue = "click to show venue photos";
				picture.style.display = 'none';
			}
			
		}
	</script>

    <!--Load the API from the specified URL
    * The async attribute allows the browser to render the page while the API loads
    * The key parameter will contain your own API key (which is not needed for this tutorial)
    * The callback parameter executes the initMap() function
    -->


  </body>
</html>
