import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  private formData = new BehaviorSubject<any>('');
  private loc = new BehaviorSubject<any>('');
  private loading = new BehaviorSubject<any>('');
  current = this.formData.asObservable();
  currentLoc = this.loc.asObservable();
  currentLoad = this.loading.asObservable();
  constructor() { }
  changeData(data) {
    this.formData.next(data);
  }
  changeLoc(loc) {
    this.loc.next(loc);
  }
  changeLoad(load) {
    this.loading.next(load);
  }
  clearData(): void {
    this.formData.next('');
  }
}
