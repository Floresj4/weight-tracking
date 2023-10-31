export interface Weight {
    date: string,
    value: number
}

export interface WeightAnnual {
    year: number,
    data: Weight[]
}