import { Component, ViewChild } from '@angular/core';
import { DataService } from './services/data.service';
import { Weight, WeightAnnual } from './model/weight.model';

import { Table } from 'primeng/table';

interface SelectableYear {
  label: string;
  value: number;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [DataService]
})
export class AppComponent {

  selectableYears: SelectableYear[] = []
  selectedYear: number = 0

  weights: Weight[] = []
  weightMin: Weight = <Weight>{}
  weightMax: Weight = <Weight>{}
  weightAvg: string = ''

  isDataLoaded: boolean = false

  @ViewChild('weightsTable')
  weightsTable: Table | undefined

  month = ["January", "February", "March",
    "April", "May", "June", "July", "August",
    "September", "October", "November",
    "December"]

  constructor(private data: DataService) {
  }

  ngOnInit() {
    this.getAvailableYears()
  }

  applyGlobalFilter(event: any, searchType: string) {
    this.weightsTable!.filterGlobal((event.target as HTMLInputElement).value, searchType)
  }

  clear(table: Table) {
    table.clear()
  }

  getAvailableYears() {
      //collect years available to review
      this.data.getYearsAvailable().subscribe((response: number[]) => {

        response.forEach((year) => {
          this.selectableYears.push({
            label: `${year}`,
            value: year
          })
        })

        //if unset
        if(this.selectedYear == 0) {
          this.selectedYear = this.selectableYears[0].value
        }

        this.getEntriesForYear(this.selectedYear)
      })
  }

  //TODO find the missing change event type
  getEntriesForSelectedYear(event: any) {
    this.getEntriesForYear(event.value)
  }

  private getEntriesForYear(year: number) {
    this.isDataLoaded = false

    this.data.getEntriesForYear(year)
      .subscribe((response: WeightAnnual) => {
        this.weights = response.data
        this.isDataLoaded = true

        this.updateTrendData(this.weights)
      })
  }

  getDate(weight: Weight) {
    return weight.date
  }

  getMonth(num: number) {
    return this.month[num - 1]
  }

  getValue(weight: Weight) {
    return weight.value
  }

  updateTrendData(weights: Weight[]) {
    if(weights.length < 1) {
      return
    }

    let min: Weight = weights[0]
    let max: Weight = weights[0]
    let avg: number = weights[0].value

    for(let i = 1; i < weights.length; i++) {
      let current = weights[i]

      if(current.value < min.value) {
        min = current
      }

      if(current.value > max.value) {
        max = current
      }

      avg += current.value
    }

    avg /= weights.length

    this.weightMin = min
    this.weightMax = max
    this.weightAvg = avg.toFixed(2)
  }

}
