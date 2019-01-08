import { Component, OnInit, Input } from '@angular/core';
import { Event } from '../event';
import { DataService } from '../data.service';
import { EventService } from '../event.service';
import { GlobalApp } from '../global';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.css']
})
export class EventsComponent implements OnInit {

  formData;
  events: Event[];
  selectedIndex;
  selectedId;
  loc = false;
  constructor(private dataService: DataService, private eventService: EventService, public global: GlobalApp,
    private router: Router,
    private route: ActivatedRoute) { }

  ngOnInit() {
    // this.selectedIndex = localStorage.getItem('selected') ? Number(localStorage.getItem('selected')) : -1;
    this.route.paramMap
      .subscribe(params => {
        this.loc = params.get('loc') === 'favorite';
      });
    this.dataService.current.subscribe(data => {
      this.selectedIndex = localStorage.getItem('selected') ? Number(localStorage.getItem('selected')) : -1;
      this.selectedId = localStorage.getItem('selected2') ? localStorage.getItem('selected2') : null;
      this.formData = data;
      if (this.formData !== '') {
        this.dataService.changeLoad('loading');
        this.eventService.getEventsList(this.formData)
        .subscribe((response: any[]) => {
          this.events = response;
          console.log(this.events);
          this.dataService.changeLoad('');
        },
          error => {console.log('error', error);
          this.dataService.changeLoad(''); }
        );
        // this.loading = false;
      } else {
        this.events = null;
      }
    });
  }

  convertShortName(name: string): string {
    if (name.length <= 35) {
      return name;
    }
    let pos = 35;
    while (name.charAt(pos) === ' ') {
      pos--;
    }
      return name.substring(0, pos).concat('...');
  }

  toggleFavorite(event: any) {
    if (localStorage.getItem(event.id) === null) {
      localStorage.setItem(event.id, JSON.stringify(event));
    } else {
      localStorage.removeItem(event.id);
    }
  }
  setIndex(i) {
    this.selectedIndex = i;
    localStorage.setItem('selected', i);
  }
  setId(id) {
    this.selectedId = id;
    localStorage.setItem('selected2', id);
  }
  goForward(id) {
    this.router.navigate(['/events', id], { relativeTo: this.route, skipLocationChange: true });
  }
  delete(key) {
    localStorage.removeItem(key);
  }
  goToDetails(eventId, loc) {
    console.log('lll');
    this.router.navigate(['/events/' + eventId, {loc: loc}]);
  }
}
