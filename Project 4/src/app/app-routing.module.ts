import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { EventsComponent } from './events/events.component';
import { EventDetailComponent } from './event-detail/event-detail.component';

const routes: Routes = [
  { path: '',
  redirectTo: '/events',
  pathMatch: 'full'
},
  {path: 'events', component: EventsComponent, data: {animation: 'EventsPage'}},
  {path: 'events/:id', component: EventDetailComponent, data: {animation: 'DetailPage'}}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
