import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import { MatButtonModule, MatFormFieldModule, MatInputModule, MatRippleModule } from '@angular/material';
import { HttpClientModule } from '@angular/common/http';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RoundProgressModule } from 'angular-svg-round-progressbar';
import { AgmCoreModule } from '@agm/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { EventSearchFormComponent } from './event-search-form/event-search-form.component';
import { EventsComponent } from './events/events.component';
import { EventDetailComponent } from './event-detail/event-detail.component';

@NgModule({
  declarations: [
    AppComponent,
    EventSearchFormComponent,
    EventsComponent,
    EventDetailComponent
  ],
  imports: [
    FormsModule, AgmCoreModule.forRoot({apiKey: 'AIzaSyCk4BpaxE_kQc8V4BGt_NA8UTe8wojz8gI'}),
    BrowserModule,
    ReactiveFormsModule, HttpClientModule, MatTooltipModule, RoundProgressModule,
    BrowserAnimationsModule, MatButtonModule, MatAutocompleteModule, MatFormFieldModule, MatInputModule, MatRippleModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
