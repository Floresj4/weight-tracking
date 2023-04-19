import { Component, Input, OnInit } from '@angular/core';
import { WeightMonthlyAvg } from 'src/app/model/weight-monthly-avg';

@Component({
  selector: 'app-year-display',
  templateUrl: './year-display.component.html',
  styleUrls: ['./year-display.component.scss']
})
export class YearDisplayComponent implements OnInit {

  @Input('year-data')
  yearData: WeightMonthlyAvg = <WeightMonthlyAvg>{}
  
  months: string[] = ['January', 'February', 'March',
  'April', 'May', 'June', 'July', 'August',
  'September', 'October', 'November', 'December']
  
  constructor() { }

  ngOnInit(): void {
  }

  getEntryMonth(index: string) {
    return this.months[(+index) - 1]
  }
}
