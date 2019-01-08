import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { EventService } from '../event.service';
import { AttachSession } from 'protractor/built/driverProviders';
import { FormControl, FormGroup, FormBuilder } from '@angular/forms';
import { GlobalApp } from '../global';
import { DataService } from '../data.service';
import {
  trigger,
  state,
  style,
  animate, group,
  transition, query, animateChild
} from '@angular/animations';

@Component({
  selector: 'app-event-detail',
  templateUrl: './event-detail.component.html',
  styleUrls: ['./event-detail.component.css'],
  animations: [    trigger('openClose', [
    // ...
    state('open', style({
      opacity: 1,
      display: 'block'
    })),
    state('closed', style({
      opacity: 0,
      display: 'none'
    })),
    transition('open => closed', [
      animate('0.2s ease-in-out')
    ]),
    transition('closed => open', [
      animate('0.5s ease-in-out')
    ]),
  ]),
  trigger('fadeInOut', [
    transition(':enter', [
      style({ opacity: 0 }),
      animate('5s', style({ opacity: 1 })),
    ]),
    transition(':leave', [
      animate('5s', style({ opacity: 0 }))
    ])
  ]), ]
})
export class EventDetailComponent implements OnInit {

  event: any;
  artists: any[];
  venue: any;
  lat: number;
  lon: number;
  upcomingEvents: any[];
  show = 'Show More';
  sortForm: FormGroup;
  originalEvents: any[];
  loc;
  twitter;
  loading = false;
  selectedImg;
  constructor(
    private fb: FormBuilder, private router: Router,
    private route: ActivatedRoute,
    private eventService: EventService,
    private location: Location, public global: GlobalApp, private dataService: DataService) {}

  ngOnInit() {
    this.dataService.currentLoad
      .subscribe(data => this.loading = data === 'loading');
    this.sortForm = this.fb.group({
      sortType: ['Default'],
      sortOrder: ['Ascending']
    });
    this.sortForm.get('sortOrder').disable();
    this.sortForm.get('sortType').valueChanges
      .subscribe(changedValue => {
        if (changedValue === 'Default') {
          this.sortForm.get('sortOrder').disable();
          this.upcomingEvents = this.originalEvents.slice();
        } else {
          this.sortForm.get('sortOrder').enable();
          if (changedValue === 'Event Name') {
            this.upcomingEvents.sort((e1 , e2) => {
              return e1.displayName.localeCompare(e2.displayName);
            });
          } else if (changedValue === 'Artist') {
            this.upcomingEvents.sort((e1, e2) => e1.performance[0].displayName.localeCompare(e2.performance[0].displayName));
          } else if (changedValue === 'Time') {
            this.upcomingEvents.sort((e1, e2) => e1.start.date.localeCompare(e2.start.date));
          } else if (changedValue === 'Type') {
            this.upcomingEvents.sort((e1, e2) => e1.type.localeCompare(e2.type));
          }
          if (this.sortForm.get('sortOrder').value === 'Descending') {
            this.upcomingEvents.reverse();
          }

        }
        console.log(changedValue);
      });
      this.sortForm.get('sortOrder').valueChanges
        .subscribe(changedValue => this.upcomingEvents.reverse());
      this.getEvent();
  }

  getEvent() {
    this.dataService.changeLoad('loading');
    const id = this.route.snapshot.paramMap.get('id');
    this.route.paramMap
      .subscribe(params => {
        console.log(params.get('loc'));
        this.loc = params.get('loc');
      });
    this.eventService.getEvent(id)
      .subscribe(event => {
        console.log(event);
        this.event = event;
        if (event._embedded && event._embedded.venues) {
          this.venue = event._embedded.venues[0];
          this.lat = Number(this.venue.location.latitude);
          this.lon = Number(this.venue.location.longitude);
        }
        this.twitter = 'https://twitter.com/intent/tweet?text=' + encodeURI(event.twitter);
        this.artists = [];
        if (event._embedded && event._embedded.attractions) {
          const attrs = event._embedded.attractions;
          for (let i = 0; i < attrs.length; i++) {
             const isMusic = event.classifications && event.classifications[0].segment ?
             event.classifications[0].segment.name === 'Music' : false;
              this.eventService.getArtist(attrs[i].name, isMusic)
              .subscribe(artist => {
                if (artist != null) {
                  this.artists.push(artist);
                }
              });
          }
        }
        this.eventService.getUpcomingEvents(this.venue.name)
          .subscribe(events => {
            console.log(events);
            if (events.error) {
              this.upcomingEvents = null;
            } else {
              this.upcomingEvents = events;
              if (events !== null) {
                this.originalEvents = events.slice();
              }
            }
            this.dataService.changeLoad('');
          });
      });
  }

  goBack() {
    // this.location.back();
    this.router.navigate(['/events', {loc: this.loc}]);
    // this.router.navigate(['/events'], { relativeTo: this.route, skipLocationChange: true });
  }

  getArtist(attractions: any[]): string {
    let res = '';
    for (let i = 0; i < attractions.length; i++) {
      res = res.concat(attractions[i].name);
      if (i !== attractions.length - 1) {
        res = res.concat(' | ');
      }
    }
    return res;
  }

  toggleShow(): void {
    if (this.show === 'Show More') {
      this.show = 'Show Less';
      return;
    }
    this.show = 'Show More';
  }
  toggleFavorite(event: any) {
    if (localStorage.getItem(event.id) === null) {
      localStorage.setItem(event.id, JSON.stringify(event));
    } else {
      localStorage.removeItem(event.id);
    }
  }
  showImage(image) {
    console.log(image.link);
    const modal = document.getElementById('myModal');
    modal.style.display = 'block';
    this.selectedImg = image.link;
  }
  closeImage() {
    document.getElementById('myModal').style.display = 'none';
  }
}
