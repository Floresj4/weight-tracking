import { Injectable } from "@angular/core";
import { WeightEntry } from "./model/weight-entry.model";

@Injectable({
  providedIn: 'root'
})
export class WeightEntryService {

    SAMPLE_ENTRY_DATA: WeightEntry[] = [
        {
            value: 160,
            date: "2001-01-01"
        },
        {
            value: 159,
            date: '2001-01-08'
        },
        {
            value: 158.6,
            date: '2001-01-15'
        },
        {
            value: 159.2,
            date: '2001-01-22'
        },
        {
            value: 158,
            date: '2001-01-29'
        },
        {
            value: 157.5,
            date: '2001-02-05'
        },
        {
            value: 156.8,
            date: '2001-02-12'
        },
        {
            value: 155.9,
            date: '2001-02-19'
        },
        {
            value: 155.5,
            date: '2001-02-26'
        },
        {
            value: 154.8,
            date: '2001-03-05'
        },
        {
            value: 154.2,
            date: '2001-03-12'
        },
        {
            value: 153.9,
            date: '2001-03-19'
        },
        {
            value: 152.3,
            date: "2001-03-26"
        },
        {
            value: 161.7,
            date: "2001-04-02"
        },
        {
            value: 155.8,
            date: "2001-04-09"
        },
        {
            value: 157.2,
            date: "2001-04-16"
        },
        {
            value: 151.9,
            date: "2001-04-23"
        },
        {
            value: 162.1,
            date: "2001-04-30"
        },
        {
            value: 153.5,
            date: "2001-05-07"
        },
        {
            value: 158.6,
            date: "2001-05-14"
        },
        {
            value: 152.8,
            date: "2001-05-21"
        },
        {
            value: 160.2,
            date: "2001-05-28"
        },
        {
            value: 154.7,
            date: "2001-06-04"
        },
        {
            value: 156.3,
            date: "2001-06-11"
        },
        {
            value: 151.6,
            date: "2001-06-18"
        },
        {
            value: 162.7,
            date: "2001-06-25"
        },
        {
            value: 153.1,
            date: "2001-07-02"
        },
        {
            value: 159.4,
            date: "2001-07-09"
        },
        {
            value: 152.0,
            date: "2001-07-16"
        },
        {
            value: 161.2,
            date: "2001-07-23"
        },
        {
            value: 155.3,
            date: "2001-07-30"
        },
        {
            value: 157.8,
            date: "2001-08-06"
        },
        {
            value: 151.7,
            date: "2001-08-13"
        },
        {
            value: 162.3,
            date: "2001-08-20"
        },
        {
            value: 153.9,
            date: "2001-08-27"
        },
        {
            value: 158.1,
            date: "2001-09-03"
        },
        {
            value: 152.5,
            date: "2001-09-10"
        },
        {
            value: 160.8,
            date: "2001-09-17"
        },
        {
            value: 154.2,
            date: "2001-09-24"
        },
        {
            value: 156.7,
            date: "2001-10-01"
        },
        {
            value: 151.8,
            date: "2001-10-08"
        },
        {
            value: 163.0,
            date: "2001-10-15"
        },
        {
            value: 153.2,
            date: "2001-10-22"
        },
        {
            value: 159.7,
            date: "2001-10-29"
        },
        {
            value: 152.1,
            date: "2001-11-05"
        },
        {
            value: 161.5,
            date: "2001-11-12"
        },
        {
            value: 155.6,
            date: "2001-11-19"
        },
        {
            value: 157.1,
            date: "2001-11-26"
        },
        {
            value: 151.5,
            date: "2001-12-03"
        },
        {
            value: 162.9,
            date: "2001-12-10"
        },
        {
            value: 153.6,
            date: "2001-12-17"
        },
        {
            value: 158.9,
            date: "2001-12-24"
        },
        {
            value: 152.7,
            date: "2001-12-31"
        },
        {
            value: 160.1,
            date: "2002-01-07"
        },
        {
            value: 154.4,
            date: "2002-01-14"
        },
        {
            value: 156.9,
            date: "2002-01-21"
        },
        {
            value: 151.4,
            date: "2002-01-28"
        },
        {
            value: 162.5,
            date: "2002-02-04"
        },
        {
            value: 153.3,
            date: "2002-02-11"
        },
        {
            value: 159.2,
            date: "2002-02-18"
        },
        {
            value: 152.2,
            date: "2002-02-25"
        },
        {
            value: 161.8,
            date: "2002-03-04"
        },
        {
            value: 155.1,
            date: "2002-03-11"
        },
        {
            value: 157.6,
            date: "2002-03-18"
        },
        {
            value: 151.3,
            date: "2002-03-25"
        },
        {
            value: 162.6,
            date: "2002-04-01"
        },
        {
            value: 153.8,
            date: "2002-04-08"
        },
        {
            value: 158.3,
            date: "2002-04-15"
        },
        {
            value: 152.6,
            date: "2002-04-22"
        },
        {
            value: 160.6,
            date: "2002-04-29"
        },
        {
            value: 154.1,
            date: "2002-05-06"
        },
        {
            value: 156.2,
            date: "2002-05-13"
        },
        {
            value: 151.2,
            date: "2002-05-20"
        },
        {
            value: 162.2,
            date: "2002-05-27"
        },
        {
            value: 153.4,
            date: "2002-06-03"
        },
        {
            value: 159.0,
            date: "2002-06-10"
        },
        {
            value: 152.9,
            date: "2002-06-17"
        },
        {
            value: 161.3,
            date: "2002-06-24"
        },
        {
            value: 155.7,
            date: "2002-07-01"
        },
        {
            value: 157.4,
            date: "2002-07-08"
        },
        {
            value: 151.1,
            date: "2002-07-15"
        },
        {
            value: 162.8,
            date: "2002-07-22"
        },
        {
            value: 153.0,
            date: "2002-07-29"
        },
        {
            value: 159.5,
            date: "2002-08-05"
        },
        {
            value: 152.4,
            date: "2002-08-12"
        },
        {
            value: 161.9,
            date: "2002-08-19"
        },
        {
            value: 155.2,
            date: "2002-08-26"
        },
        {
            value: 157.9,
            date: "2002-09-02"
        },
        {
            value: 151.0,
            date: "2002-09-09"
        },
        {
            value: 162.4,
            date: "2002-09-16"
        },
        {
            value: 153.7,
            date: "2002-09-23"
        },
        {
            value: 158.7,
            date: "2002-09-30"
        },
        {
            value: 152.1,
            date: "2002-10-07"
        },
        {
            value: 160.9,
            date: "2002-10-14"
        },
        {
            value: 154.5,
            date: "2002-10-21"
        },
        {
            value: 156.8,
            date: "2002-10-28"
        },
        {
            value: 151.9,
            date: "2002-11-04"
        },
        {
            value: 163.0,
            date: "2002-11-11"
        },
        {
            value: 153.2,
            date: "2002-11-18"
        },
        {
            value: 159.6,
            date: "2002-11-25"
        },
        {
            value: 152.3,
            date: "2002-12-02"
        },
        {
            value: 161.6,
            date: "2002-12-09"
        },
        {
            value: 155.9,
            date: "2002-12-16"
        },
        {
            value: 157.3,
            date: "2002-12-23"
        },
        {
            value: 151.8,
            date: "2002-12-30"
        },
        {
            value: 162.0,
            date: "2003-01-06"
        },
        {
            value: 153.5,
            date: "2003-01-13"
        },
        {
            value: 158.4,
            date: "2003-01-20"
        },
        {
            value: 152.7,
            date: "2003-01-27"
        },
        {
            value: 160.3,
            date: "2003-02-03"
        },
        {
            value: 154.0,
            date: "2003-02-10"
        },
        {
            value: 156.5,
            date: "2003-02-17"
        }
    ]

    getAverageEntry(): WeightEntry {
        const numberOfEntries = this.SAMPLE_ENTRY_DATA.length
        const total = this.SAMPLE_ENTRY_DATA.reduce((sum, entry) => 
            sum + entry.value, 0);

        const average = total / this.SAMPLE_ENTRY_DATA.length
        return { 
            value: parseFloat(average.toFixed(2)), 
            date: ''
        }
    }

    getEntries(): WeightEntry[] {
        return this.SAMPLE_ENTRY_DATA;
    }

    getHighestEntry(): WeightEntry {
        return this.SAMPLE_ENTRY_DATA.reduce((prev, curr) => 
            (prev.value > curr.value) 
                ? prev : curr);
    }

    getLowestEntry(): WeightEntry {
        return this.SAMPLE_ENTRY_DATA.reduce((prev, curr) => 
            (prev.value < curr.value) 
                ? prev : curr);
    }
}