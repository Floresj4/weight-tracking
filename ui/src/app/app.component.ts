import { Component } from '@angular/core';
import { Trend } from './model/trend.model';
import { WeightEntries, WeightMonthlyAvg } from './model/weight-monthly-avg';
import { DataService } from './services/data.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [DataService]
})
export class AppComponent {

  selectedYear: number = 0
  selectableYears: number[] = []

  yearEntries: WeightEntries = <WeightEntries>{}

  trendData: Trend = <Trend>{}
  displayTrendData: boolean = false

  monthlyAvg: WeightMonthlyAvg = <WeightMonthlyAvg>{}

  constructor(private data: DataService) {

    this.getAvailableYears()

    this.getTrendForYear(2022)

    this.getMonthlyAvgForYear(2022)

    this.getEntriesForYear(2022)
  }

  getAvailableYears() {
      //collect years available to review
      this.data.getYearsAvailable().subscribe((response: number[]) => {
        this.selectableYears = response
        this.selectedYear = this.selectableYears[1]
      })
  }

  getEntriesForYear(year: number) {
    this.data.getEntriesForYear(year).subscribe((response: WeightEntries) => {
      this.yearEntries = response
    })
  }

  getTrendForYear(year: number) {
    this.data.getTrendForYear(year).subscribe((response: Trend) => {
      this.trendData = response
      this.displayTrendData = true
    })
  }
  
  getMonthlyAvgForYear(year: number) {
    this.data.getMonthlyAvgForYear(year).subscribe((response: any) => {
      this.monthlyAvg = response
    })
  }
}
