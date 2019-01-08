import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { slideInAnimation } from './animation';
import { EventService } from './event.service';
import { DataService } from './data.service';
import { GlobalApp } from './global';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  animations: [
    slideInAnimation
  ]
})
export class AppComponent implements OnInit {
  loading = false;
  loc = false;
  // formData: any;
  // events: any;
  // constructor(private eventService: EventService, private dataService: DataService) {}
  constructor(public global: GlobalApp, private router: Router, private route: ActivatedRoute, private dataService: DataService) {}
  ngOnInit() {
    console.log('root');
    // this.selectedIndex = localStorage.getItem('selected') ? Number(localStorage.getItem('selected')) : -1;
    this.dataService.currentLoc
      .subscribe(data => this.loc = data === 'favorite');
    this.dataService.currentLoad
      .subscribe(data => this.loading = data === 'loading');
    // this.route.paramMap
    //   .subscribe(params => {
    //     this.loc = params.get('loc') === 'favorite';
    //   });
    }
  prepareRoute(outlet: RouterOutlet) {
    return outlet && outlet.activatedRouteData && outlet.activatedRouteData['animation'];
  }
  showList(loc) {
    this.loc = loc === 'favorite';
    this.router.navigate(['/events', {loc: loc}]);
  }

  // ngOnInit() {
  //   this.dataService.current.subscribe(data => {
  //     this.formData = data;
  //     console.log('reload');
  //     if (this.formData !== '') {
  //       this.eventService.getEventsList(this.formData)
  //       .subscribe((response: any[]) => {
  //         this.events = response;
  //         // console.log(this.events);
  //       },
  //         error => console.log('error', error)
  //       );
  //       // this.loading = false;
  //     }
  //   });
  // }

}
