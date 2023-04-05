import { Component, Input, OnInit } from '@angular/core';

import { Trend } from 'src/app/model/trend.model';

@Component({
  selector: 'app-trend-display',
  templateUrl: './trend-display.component.html',
  styleUrls: ['./trend-display.component.scss']
})
export class TrendDisplayComponent implements OnInit {

  @Input()
  trend: Trend = <Trend>{}

  constructor() { }

  ngOnInit(): void {
  }

  getMin() {
    return 0
  }
}
