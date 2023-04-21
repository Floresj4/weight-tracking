import { WeightEntry } from "./weight-entry.model";

export interface WeightMonthlyAvg {
    year: number,
    data: WeightEntry[]
}

export interface WeightEntries {
    year: number,
    data: WeightEntry[]
}