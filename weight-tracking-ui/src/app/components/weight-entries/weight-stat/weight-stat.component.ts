import { Component, input } from '@angular/core';
import { DatePipe } from '@angular/common';

import { Card } from "../../shared/card/card";

import { WeightStat as WeightStatModel } from '../model/weight-stat.model';

@Component({
  selector: 'app-weight-stat',
  standalone: true,
  imports: [Card, DatePipe],
  templateUrl: './weight-stat.component.html',
  styleUrl: './weight-stat.component.scss'
})
export class WeightStatComponent {

  entry = input.required<WeightStatModel>()

}
