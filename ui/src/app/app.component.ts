import { Component, ViewChild } from '@angular/core';
import { DataService } from './services/data.service';
import { Weight, WeightAnnual } from './model/weight.model';

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

  isDataLoaded: boolean = false

  @ViewChild('weightsTable')
  weightsTable: Table | undefined

  dataz: any = <any>{}
  options: any = <any>{}

  month = ["January", "February", "March",
    "April", "May", "June", "July", "August",
    "September", "October", "November",
    "December"]

  constructor(private data: DataService) {
  }

  ngOnInit() {
    this.getAvailableYears()

    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

    this.dataz = {
        labels: this.month,
        datasets: [
            {
                label: 'Average',
                data: [65, 59, 80, 81, 56, 55, 40, 33, 54, 84, 21, 90],
                fill: false,
                borderColor: documentStyle.getPropertyValue('--blue-500'),
                tension: 0.4
            },
            {
                label: 'High',
                data: [28, 48, 40, 19, 86, 27, 90, 54, 21, 76, 23, 92],
                fill: false,
                borderColor: documentStyle.getPropertyValue('--pink-500'),
                tension: 0.4
            }
        ]
    };

    this.options = {
        maintainAspectRatio: false,
        aspectRatio: 0.6,
        plugins: {
            legend: {
                labels: {
                    color: textColor
                }
            }
        },
        scales: {
            x: {
                ticks: {
                    color: textColorSecondary
                },
                grid: {
                    color: surfaceBorder,
                    drawBorder: false
                }
            },
            y: {
                ticks: {
                    color: textColorSecondary
                },
                grid: {
                    color: surfaceBorder,
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
