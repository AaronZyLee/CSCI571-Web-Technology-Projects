import { Injectable } from '@angular/core';
@Injectable({
    providedIn: 'root'
  })
export class GlobalApp {

    constructor() {}
    public localStorageItem(id: string): string {
        return localStorage.getItem(id);
    }
    public getKeys() {
        const keys = [];
        Object.entries(localStorage).forEach(
            ([key, value]) => {
                if (key !== 'selected' && key !== 'selected2') {
                    keys.push(key);
                }
            }
          );
        return keys;
    }
    public getEvent(id: string): string {
        return JSON.parse(localStorage.getItem(id));
    }
}
