import { Component, ViewChild } from '@angular/core';
import { DataService } from './services/data.service';
import { Weight, WeightAnnual, WeightPresentation } from './model/weight.model';

import { Table } from 'primeng/table';
import { TreeNode } from 'primeng/api';

interface SelectableYear {
  label: string
  value: number
}

interface TrendData {
  minimum: Weight
  maximum: Weight
  average: string
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

  presentationMin: number[] = []
  presentationMax: number[] = []
  presentationAvg: number[] = []

  isDataLoaded: boolean = false

  @ViewChild('weightsTable')
  weightsTable: Table | undefined

  presentation: any = <any>{}
  options: any = <any>{}

  documentStyle = getComputedStyle(document.documentElement);
  textColor = this.documentStyle.getPropertyValue('--text-color');
  textColorSecondary = this.documentStyle.getPropertyValue('--text-color-secondary');
  surfaceBorder = this.documentStyle.getPropertyValue('--surface-border');

  month = ["January", "February", "March",
    "April", "May", "June", "July", "August",
    "September", "October", "November",
    "December"]

  constructor(private data: DataService) {
  }

  ngOnInit() {
    this.getAvailableYears()



    this.options = {
        maintainAspectRatio: false,
        aspectRatio: 0.6,
        plugins: {
            legend: {
                labels: {
                    color: this.textColor
                }
            }
        },
        scales: {
            x: {
                ticks: {
                    color: this.textColorSecondary
                },
                grid: {
                    color: this.surfaceBorder,
                    drawBorder: false
                }
            },
            y: {
                ticks: {
                    color: this.textColorSecondary
                },
                grid: {
                    color: this.surfaceBorder,
                    drawBorder: false
                }
            }
        }
    };

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

        let trend: TrendData = this.updateTrendData(this.weights)
        this.weightMin = trend.minimum ?? <Weight>{}
        this.weightMax = trend.maximum ?? <Weight>{}
        this.weightAvg = trend.average ?? ''
      })
    
      this.data.getPresentationData(year)
        .subscribe((response: WeightPresentation) => {
          this.presentation = this.getPresentationData(response)
        })
  }

  getDate(weight: Weight) {
    return weight.date
  }

  getMonth(num: number) {
    return this.month[num - 1]
  }

  getPresentationData(response: WeightPresentation) {

    return {
      labels: this.month,
      datasets: [
          {
              label: 'Average',
              data: response.data.average,
              fill: false,
              borderColor: this.documentStyle.getPropertyValue('--blue-500'),
              tension: 0.4
          },
          {
              label: 'High',
              data: response.data.maximum,
              fill: false,
              borderColor: this.documentStyle.getPropertyValue('--pink-500'),
              tension: 0.4
          }
      ]
  };

  }

  getValue(weight: Weight) {
    return weight.value
  }

  updateTrendData(weights: Weight[]) {
    if(weights.length < 1) {
      return <TrendData>{}
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

    return <TrendData> {
      minimum: min,
      maximum: max,
      average: avg.toFixed(2)
    }
  }

}
