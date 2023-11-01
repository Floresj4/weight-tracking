import { Component } from '@angular/core';
import { DataService } from './services/data.service';
import { Weight, WeightAnnual } from './model/weight.model';

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

  weight: Weight[] = []

  isDataLoaded: boolean = false

  constructor(private data: DataService) {
  }

  ngOnInit() {
    this.getAvailableYears()
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

        this.data.getEntriesForYear(this.selectedYear)
          .subscribe((response: WeightAnnual) => {
            this.weight = response.data
            this.isDataLoaded = true
          })
      })
  }

  //TODO find the missing change event type
  getEntriesForSelectedYear(event: any) {
    this.isDataLoaded = false

    this.data.getEntriesForYear(this.selectedYear)
      .subscribe((response: WeightAnnual) => {
        this.weight = response.data
        this.isDataLoaded = true
      })
  }
}
