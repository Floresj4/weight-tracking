import { Injectable } from "@angular/core";

import { User } from "./model/user.model";

@Injectable({
  providedIn: 'root'
})
export class UserService {

    private SAMPLE_USERS_DATA: User[] = [
        {
            id: "1",
            name: "Jason",
            avatar: "avatar.png"
       }
    ]

    getUsers(): User[] {
        return this.SAMPLE_USERS_DATA;
    }

    getUserById(id: string): User | undefined {
        return this.SAMPLE_USERS_DATA.find(user => user.id === id);
    }
}