import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { EventService } from '../event.service';
import { Location } from '../location';
import { Event } from '../event';
import { DataService } from '../data.service';
import { Router } from '@angular/router';
import { debounceTime } from 'rxjs/operators';

@Component({
  selector: 'app-event-search-form',
  templateUrl: './event-search-form.component.html',
  styleUrls: ['./event-search-form.component.css']
})
export class EventSearchFormComponent implements OnInit {
  eventForm: FormGroup;
  options = [];
  categories = ['Music', 'Sports', 'Arts & Theatre', 'Film', 'Miscellaneous'];
  events: any[];
  loading = false;
  constructor(private fb: FormBuilder, private eventService: EventService, private dataService: DataService,
    private router: Router) {}
  ngOnInit() {
    localStorage.removeItem('selected');
    localStorage.removeItem('selected2');
    this.eventForm = this.fb.group({
      keyword: ['', Validators.required],
      category: ['All'],
      distance: ['10'],
      location: ['local'],
      locationKey: [{value: '', disabled: true}],
      lat: [''], lon: [''], unit: ['miles']
    });
    this.eventForm.get('keyword').valueChanges.pipe(debounceTime(500))
      .subscribe(keywordChanged => {
        console.log(keywordChanged);
        if (keywordChanged !== '') {
          this.eventService.getSuggestions(keywordChanged)
          .subscribe(data => {
            if (data !== '') {
              const temp = [];
              data.forEach(function(attr) {
                temp.push(attr.name);
              });
              this.options = temp;
            }
          });
        }
      });
    this.eventForm.get('location').valueChanges
      .subscribe(checkedValue => {
        const locationKey = this.eventForm.get('locationKey');
        if (checkedValue === 'local') {
          locationKey.disable();
        } else if (checkedValue === 'other') {
          locationKey.setValidators(Validators.required);
          locationKey.enable();
        }
      });
    this.eventService.getCurrentLoc()
        .subscribe((data: Location) => {
          this.eventForm.get('lat').setValue(data.lat);
          this.eventForm.get('lon').setValue(data.lon);
          // this.eventForm.get('submitBtn').enable();
        });

  }
  onSubmit() {
    localStorage.setItem('selected', '-1');
    if (this.eventForm.valid) {
      // console.log(this.eventForm.value);
      this.dataService.changeData(this.eventForm.value);
      this.dataService.changeLoc('');
      // this.loading = true;
      // this.eventService.getEventsList(this.eventForm.value)
      //   .subscribe((response: any[]) => {
      //     // console.log('success', response);
      //     this.events = response;
      //     console.log(this.events);
      //   },
      //             error => console.log('error', error)
      //   );
      //   this.loading = false;
    } else {
      // Object.keys(this.eventForm.controls).forEach(field => { // {1}
      //   const control = this.eventForm.get(field);            // {2}
      //   control.markAsTouched({ onlySelf: true });       // {3}
      // });
    }
  }
  update() {
    this.dataService.clearData();
    this.router.navigate(['/events', {loc: 'results'}]);
    this.dataService.changeLoc('');
    localStorage.removeItem('selected');
    localStorage.removeItem('selected2');
    this.ngOnInit();
    (<HTMLInputElement>document.getElementById('locationInput')).disabled = true;
  }

}
