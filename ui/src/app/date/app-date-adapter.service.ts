import {NativeDateAdapter} from "@angular/material/core";
import {Injectable} from "@angular/core";

@Injectable()
export class AppDateAdapter extends NativeDateAdapter {
  override parse(value: string) {
    let it = value.split('/');
    if (it.length == 3) {
      let year = +it[2];
      if (year < 100) // Converts xx into 20xx instead of 19xx
        year += 2000;
      return new Date(year, +it[1] - 1, +it[0])
    } else return null
  }

  override format(date: Date, displayFormat: Object) {
    return ('0' + date.getDate()).slice(-2) + '/' +
      ('0' + (date.getMonth() + 1)).slice(-2) + '/' + date.getFullYear()
  }
}
