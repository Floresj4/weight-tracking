import { Component, input } from '@angular/core';

import { Card } from "../../../components/shared/card/card";

import { WeightStat as WeightStatModel } from '../model/weight-stat.model';

@Component({
  selector: 'app-weight-stat',
  standalone: true,
  imports: [Card],
  templateUrl: './weight-stat.html',
  styleUrl: './weight-stat.scss'
})
export class WeightStat {

  entry = input.required<WeightStatModel>()

}
