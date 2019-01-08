import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Location } from './location';
@Injectable({
  providedIn: 'root'
})
export class EventService {
  currentLocURL = 'http://ip-api.com/json';
  // host = 'http://localhost:3000';
  host = '';
  constructor(private http: HttpClient) { }
  getCurrentLoc() {
    return this.http.get<Location>(this.currentLocURL);
  }
  getSuggestions(keyword: string) {
    return this.http.get<any>(this.host + '/suggestion?keyword=' + keyword);
  }
  getEventsList(formData) {
    return this.http.post(this.host + '/search', formData);
  }
  getEvent(id): any {
    return this.http.get<any>(this.host + '/events/' + id);
  }
  getArtist(artist, music: boolean): any {
    return this.http.get<any>(this.host + '/artist?name=' + artist + '&music=' + music);
  }
  getUpcomingEvents(venue): any {
    return this.http.get<any>(this.host + '/activities?name=' + venue);
  }
  // getImages(attr): any {
  //   return this.http.get<any>(this.host + '/images?q=' + attr);
  // }
}
