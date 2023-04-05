import { Component } from '@angular/core';
import { Trend } from './model/trend.model';
import { DataService } from './services/data.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [DataService]
})
export class AppComponent {
  title = 'ui';

  year: number = 0
  years: number[] = []
  entries: number[] = []
  trend: Trend = <Trend>{}
  
  constructor(private data: DataService) {

    this.getAvailableYears()

    this.getTrendForYear(2022)
  }

  getAvailableYears() {
      //collect years available to review
      this.data.getYearsAvailable().subscribe((response: number[]) => {
        this.years = response
      })
  }

  getEntriesForYear(year: number) {
    this.data.getEntriesForYear(year).subscribe((response: number[]) => {
      this.entries = response
    })
  }

  getTrendForYear(year: number) {
    this.data.getTrendForYear(year).subscribe((response: Trend) => {
      console.log(response)
      this.trend = response
    })
  }
}
