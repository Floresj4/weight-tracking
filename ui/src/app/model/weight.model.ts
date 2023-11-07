export interface Weight {
    date: string,
    month?: number,
    value: number
}

export interface WeightAnnual {
    year: number,
    data: Weight[]
}