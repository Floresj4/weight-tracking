import { WeightEntry } from "./weight-entry.model";

export interface WeightMonthlyAvg {
    year: number,
    data: WeightEntry[]
}