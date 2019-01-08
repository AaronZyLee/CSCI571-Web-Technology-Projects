var express = require('express'),
	app     = express(),
	request = require('request'),
	path    = require('path'),
	bodyParser = require('body-parser'),
	geohash = require('ngeohash'),
	fetch   = require('node-fetch'),
	dateFormat = require('dateformat'),
	SpotifyWebApi = require('spotify-web-api-node'),
	GOOGLE_API_KEY = 'AIzaSyCk4BpaxE_kQc8V4BGt_NA8UTe8wojz8gI',
	TICKET_API_KEY = 'w7cnGIumuVssskmEBdx8Uh2vblzfbtoq',
	SEARCH_ENGINE_ID = '003012504293356503690:oi8rq5eq5ty',
	SONGKICK_API_KEY = 'iU5DFPv3bniLqs6J',
	segmentDict    =  {'All': '', 'Music': 'KZFzniwnSyZfZ7v7nJ', 'Arts & Theatre': 'KZFzniwnSyZfZ7v7na', 'Film': 'KZFzniwnSyZfZ7v7nn', 'Miscellaneous': 'KZFzniwnSyZfZ7v7n1', 'Sports': 'KZFzniwnSyZfZ7v7nE'};

function fetchJSON(url) {
	return fetch(url).then(response => response.json());
}
var spotifyApi = new SpotifyWebApi({
	clientId: 'c095ce1681e04ec4904f5820edd48baf',
	clientSecret: '2b3074d0dbaf48eb9adb4778335686fa'
});
getCredential();

app.use(express.static(__dirname + '/dist/eventSearch'));
app.use(function(req, res, next) {
	res.header("Access-Control-Allow-Origin", "*");
	res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
	next();
  });
  app.use(bodyParser.json());
  app.use(bodyParser.urlencoded({extended:true}));

app.get("/", (req,res) => res.sendFile(path.join(__dirname)));

app.get("/suggestion",function(req,res){
	var keyword = req.query.keyword;
	var url = 'https://app.ticketmaster.com/discovery/v2/suggest?apikey='+TICKET_API_KEY+'&keyword='+keyword;
	console.log('suggestion');
	request({
		url:url,
		json:true
	},function(err,response,body){
		if(err)
			console.log(err);
		else if(response.statusCode === 200){
			if(body._embedded !=null && body._embedded.attractions!=null){
				res.send(body._embedded.attractions);
			}else{
				res.send('');
			}
		}
	})
})
app.get("/search", function(req,res){
	var keyword  =  req.query.keyword,
	category =  req.query.category,
	distance =  req.query.distance,
	location =  req.query.location,
	unit     =  req.query.unit,
	lat      =  req.query.lat,
	lon      =  req.query.lon;
	locationKey = '';
	var event_url = 'https://app.ticketmaster.com/discovery/v2/events.json?apikey=' + TICKET_API_KEY + '&keyword=' + encodeURI(keyword)
				+ '&segmentId=' + segmentDict[category] + '&radius=' + distance + '&unit=' + unit + '&geoPoint='
	if(location == 'other'){
		locationKey = req.query.locationKey;
		var loc_url = 'https://maps.googleapis.com/maps/api/geocode/json?key=' + GOOGLE_API_KEY + '&address=' + encodeURI(locationKey);
		request({
			url:loc_url,
			json:true
		},function(err,response,body){
			if(err)
				console.log(err);
			else if(response.statusCode === 200){
				lat = body.results[0].geometry.location.lat;
				lon = body.results[0].geometry.location.lng;
				console.log(lat,lon);
				getEventList(event_url+geohash.encode(lat,lon),res)
			}
		});
	}else{
		getEventList(event_url+geohash.encode(lat,lon),res)
	}
});
app.post("/search", function(req,res){
	//console.log(req.body);
	var keyword  =  req.body.keyword,
		category =  req.body.category,
		distance =  req.body.distance,
		location =  req.body.location,
		unit     =  req.body.unit,
		lat      =  req.body.lat,
		lon      =  req.body.lon;
		locationKey = '';
	var event_url = 'https://app.ticketmaster.com/discovery/v2/events.json?apikey=' + TICKET_API_KEY + '&keyword=' + encodeURI(keyword)
				+ '&segmentId=' + segmentDict[category] + '&radius=' + distance + '&unit=' + unit + '&geoPoint='
	if(location == 'other'){
		locationKey = req.body.locationKey;
		var loc_url = 'https://maps.googleapis.com/maps/api/geocode/json?key=' + GOOGLE_API_KEY + '&address=' + encodeURI(locationKey);
		request({
			url:loc_url,
			json:true
		},function(err,response,body){
			if(err)
				console.log(err);
			else if(response.statusCode === 200){
				lat = body.results[0].geometry.location.lat;
				lon = body.results[0].geometry.location.lng;
				console.log(lat,lon);
				getEventList(event_url+geohash.encode(lat,lon),res)
			}
		});
	}else{
		getEventList(event_url+geohash.encode(lat,lon),res)
	}
});

app.get("/events/:id", function(req,res){
	var detail_url = "https://app.ticketmaster.com/discovery/v2/events/" + req.params.id + "?apikey=" + TICKET_API_KEY;
	//console.log(detail_url)
	request({
		url:detail_url,
		json:true
	},function(err,response,body){
		if(err)
			console.log(err);
		else if(response.statusCode === 200){
			var twitter = 'Check out ' + body.name + ' located at ' + body._embedded.venues[0].name +'. Website: ' + body.url + ' CSCI571EventSearch';
			body.twitter = twitter;
			body.android = {};
			body.android.twitter = twitter;
			body.android.artists = [];
			var urls = [];
			if(body._embedded && body._embedded.venues){
				body.android.venue = {}
				if(body._embedded.venues[0].name){
					body.android.venue.name = body._embedded.venues[0].name;
				}
				if(body._embedded.venues[0].city && body._embedded.venues[0].city.name){
					body.android.venue.city = body._embedded.venues[0].city.name;
				}
				if(body._embedded.venues[0].address && body._embedded.venues[0].address.line1){
					body.android.venue.address = body._embedded.venues[0].address.line1;
				}
				if(body._embedded.venues[0].boxOfficeInfo && body._embedded.venues[0].boxOfficeInfo.phoneNumberDetail){
					body.android.venue.phoneNumber = body._embedded.venues[0].boxOfficeInfo.phoneNumberDetail;
				}
				if(body._embedded.venues[0].boxOfficeInfo && body._embedded.venues[0].boxOfficeInfo.openHoursDetail){
					body.android.venue.openHoursDetail = body._embedded.venues[0].boxOfficeInfo.openHoursDetail
				}
				if(body._embedded.venues[0].generalInfo){
					if(body._embedded.venues[0].generalInfo.generalRule){
						body.android.venue.generalRule = body._embedded.venues[0].generalInfo.generalRule;
					}
					if(body._embedded.venues[0].generalInfo.childRule){
						body.android.venue.childRule = body._embedded.venues[0].generalInfo.childRule;
					}
				}
				if(body._embedded.venues[0].location){
					body.android.venue.longitude = body._embedded.venues[0].location.longitude;
					body.android.venue.latitude = body._embedded.venues[0].location.latitude;
				}
				// body.android.venue = body._embedded.venues[0]
				//console.log("http://lizeyu-hw8.us-east-2.elasticbeanstalk.com" + "/activities?name=" + body.android.venue.name)
				urls.push("http://lizeyu-hw8.us-east-2.elasticbeanstalk.com" + "/activities?name=" + body.android.venue.name)
			}
			if(body.dates && body.dates.start){
				if(body.dates.start.localDate){
					body.android.time = dateFormat(body.dates.start.localDate,"mediumDate");
				}
				if(body.dates.start.localTime){
					body.android.time += " " + body.dates.start.localTime;
				}
				//body.android.time = dateFormat(body.dates.start.localDate,"mediumDate") + " " + body.dates.start.localTime
			}
			if(body.classifications){
				var temp = "";
				if(body.classifications[0].segment){
					temp += body.classifications[0].segment.name;
				}
				if(body.classifications[0].genre){
					temp += " | " +  body.classifications[0].genre.name;
				}
				if(temp!==""){
					body.android.category = temp;
				}
			}
			if(body.dates && body.dates.status){
				var code = body.dates.status.code
				body.android.status = code.charAt(0).toUpperCase()+code.slice(1);
			}
			if(body.url){
				body.android.url = body.url;
			}
			if(body.seatmap && body.seatmap.staticUrl){
				body.android.seatmap = body.seatmap.staticUrl;
			}
			if(body.priceRanges){
				body.android.priceRanges = "$" + (body.priceRanges[0].min.toFixed(2)).toString() + "~$" + (body.priceRanges[0].max.toFixed(2)).toString()
			}
			

			if(body._embedded && body._embedded.attractions){
				var attractions = body._embedded.attractions;
				var temp = '';
				for (var i = 0; i < attractions.length; i++) {
				  temp = temp.concat(attractions[i].name);
				  if (i !== attractions.length - 1) {
					temp = temp.concat(' | ');
				  }
				  var isMusic = body.classifications && body.classifications[0].segment && body.classifications[0].segment.name === 'Music';
				  //console.log("http://lizeyu-hw8.us-east-2.elasticbeanstalk.com/artist?name=" + attractions[i].name + "&music=" +isMusic)
				  urls.push("http://lizeyu-hw8.us-east-2.elasticbeanstalk.com/artist?name=" + attractions[i].name + "&music=" +isMusic);
				}
				
			}
			let promises = urls.map(url => fetchJSON(url));
			body.android.team = temp;
			Promise.all(promises).then(responses => {
				body.android.upcoming = responses[0];
				if(body.android.upcoming.error==null){
					var up_arr_send = [];
					var up_arr = body.android.upcoming;
					up_arr.forEach(function(event){
						var cur = {};
						cur.name = event.displayName;
						cur.type = event.type;
						cur.time = dateFormat(event.start.date,"mediumDate")
						if(event.start.time)
							cur.time += " "+ event.start.time;
						cur.time_key = event.start.date;
						cur.artist = event.performance[0].displayName;
						cur.url = event.uri;
						up_arr_send.push(cur);
					});
					body.android.upcoming = up_arr_send;
				}
				var artists_arr = responses.slice(1);
				artists_arr.forEach(function(artist){
					var cur = {};
					if(artist.name){
						cur.name = artist.name;
					}
					if(artist.profile){
						var profile = artist.profile;
						if(profile.name){
							cur.pname = profile.name;
						}
						if(profile.popularity){
							cur.popularity = profile.popularity;
						}
						if(profile.followers && profile.followers.total){
							cur.followers = profile.followers.total;
						}
						if(profile.external_urls && profile.external_urls.spotify){
							cur.spotify = profile.external_urls.spotify;
						}
					}
					if(artist.images){
						cur.images = artist.images;
					}
					body.android.artists.push(cur);
				});
				//body.android.artists = responses.slice(1);
				res.send(body);
			});
		}
	});
});

app.get("/artist", function(req,res){
	var name = req.query.name;
	var isMusic = req.query.music === 'true';
	var artist = {};
	var image_url = 'https://www.googleapis.com/customsearch/v1?q=' + encodeURI(name) + '&cx=' + SEARCH_ENGINE_ID + '&imgSize=huge&imgType=news&num=8&searchType=image&key=' + GOOGLE_API_KEY;
	artist.name = name;
	if(isMusic){
		spotifyApi.searchArtists(name)
		.then(function(data) {
		  //console.log('Search artists by "Love"', data.body);
		  var artists = data.body.artists.items;
		  var idx = 0;
		  if(artists.length != 1){
			  for(var i=0;i<artists.length;i++){
				  if(artists[i].name.toLowerCase() == name.toLowerCase()){
					  idx = i;
					  break;
				  }
			  }
		  }
		  //res.send(artists[idx]);
		  artist.profile = artists[idx];
		  request({
			url:image_url,
			json:true
			},function(err,response,body){
			if(err)
			console.log(err);
			else if(response.statusCode === 200){
			if(body.items!=null){
				//res.send(body.items);
				artist.images = body.items;
				res.send(artist);
			}
			else
				res.send({'error':'notFound'});
			}
			});
		}, function(err) {
		  console.error(err);
		  getCredential();
		});
	}else{
		request({
			url:image_url,
			json:true
			},function(err,response,body){
			if(err)
			console.log(err);
			else if(response.statusCode === 200){
			if(body.items!=null){
				//res.send(body.items);
				artist.images = body.items;
				res.send(artist);
			}
			else
				res.send({'error':'notFound'});
			}
			});
	}

});

app.get("/activities", function(req,res){
	var name = req.query.name;
	var songkick_url = 'https://api.songkick.com/api/3.0/search/venues.json?query=' + encodeURI(name) + '&apikey=' + SONGKICK_API_KEY;
	request({
		url:songkick_url,
		json:true
		},function(err,response,body){
		if(err)
			console.log(err);
		else if(response.statusCode === 200){
			if(body.resultsPage && body.resultsPage.results && body.resultsPage.results.venue){
				var id = body.resultsPage.results.venue[0].id;
				//console.log(id);
				var events_url = 'https://api.songkick.com/api/3.0/venues/' + id + '/calendar.json?apikey=' + SONGKICK_API_KEY;
				request({url: events_url, json:true}, function(err,response,body){
					if(err)	console.log(err);
					else if(response.statusCode === 200){
						if(body.resultsPage &&body.resultsPage.results && body.resultsPage.results.event)
							res.send(body.resultsPage.results.event);
						else{
							res.send({'error':'notFound'})
						}
					}
				});
			}else{
				res.send({'error':'notFound'});
			}

			}
		});
})

app.listen(process.env.PORT || 3000, process.env.IP,function(){
	console.log("Server started!")
})

function getCredential(){
	spotifyApi.clientCredentialsGrant().then(
		function(data) {
		  //console.log('The access token is ' + data.body['access_token']);
		  spotifyApi.setAccessToken(data.body['access_token']);
		},
		function(err) {
		  console.log('Something went wrong!', err);
		}
	  );
}
function getEventList(event_url,res){
	request({
	url:event_url,
	json:true
	},function(err,response,body){
	if(err)
	console.log(err);
	else if(response.statusCode === 200){
	if(body._embedded!=null){
		var events = body._embedded.events;
		events.forEach(function(event){
			event.favorite = false;
		})
		res.send(events);
	}
		
	else
		res.send({'error':'notFound'});
	}
	});
}