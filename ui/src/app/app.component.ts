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

  isYearDataLoading: boolean = false
  weights: WeightEntries = <WeightEntries>{}

  trendData: Trend = <Trend>{}
  displayTrendData: boolean = false

  minWeight = 0;
  maxWeight = 0;
  avgWeight = 0;

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
    this.isYearDataLoading = true
    this.selectedYear = year

    this.data.getEntriesForYear(year).subscribe((response: WeightEntries) => {
      this.weights = response

      //determine min, max, average from the current selection
      for(let i = 0; i < response.data.length; i++) {
        let w = response.data[i]

        if(this.minWeight == 0 || w.entryValue < this.minWeight) {
          this.minWeight = w.entryValue
        }

        if(this.maxWeight == 0 || w.entryValue > this.maxWeight) {
          this.maxWeight = w.entryValue
        }

        this.avgWeight += w.entryValue;
      }
    
      //finalize the average
      this.avgWeight /= response.data.length
      this.isYearDataLoading = false
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

  onYearChange(year: number) {
    this.getEntriesForYear(year)
  }
}
