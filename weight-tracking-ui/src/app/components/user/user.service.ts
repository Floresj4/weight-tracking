import { Injectable } from "@angular/core";

import { UserModel } from "./model/user.model";

@Injectable({
  providedIn: 'root'
})
export class UserService {

    private SAMPLE_USERS_DATA: UserModel[] = [
        {
            id: "1",
            name: "Jason",
            avatar: "avatar.png"
       }
    ]

    getUsers(): UserModel[] {
        return this.SAMPLE_USERS_DATA;
    }

    getUserById(id: string): UserModel | undefined {
        return this.SAMPLE_USERS_DATA.find(user => user.id === id);
    }
}