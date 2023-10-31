import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { environment } from 'src/environments/environment';

import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { Weight } from '../model/weight.model';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  defaultOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    }),
  }

  constructor(private http: HttpClient) {
  }

  getYearsAvailable() {

    let requestUrl = environment.url + '/years'
    return this.http.get<number[]>(requestUrl, 
      this.defaultOptions)
  }

  getEntriesForYear(year: number) {

    let requestUrl = environment.url + `/year/${year}`
    return this.http.get<Weight>(requestUrl, 
      this.defaultOptions)
  }
}
