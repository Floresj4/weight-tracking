export interface Weight {
    date: string,
    month: number,
    value: number
}

export interface WeightAnnual {
    year: number,
    data: Weight[]
}

export interface WeightPresentation {
    year: number,
    data: {
        minimum: number[],
        maximum: number[],
        average: number[]
    }
}