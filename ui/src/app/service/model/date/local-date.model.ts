export class LocalDate {

  year: number
  month: number
  day: number

  constructor(date: Date) {
    this.year = date.getFullYear();
    this.month = date.getMonth();
    this.day = date.getDate();
  }

  toString(): string {
    return [
      this.year,
      ('0' + (this.month + 1)).slice(-2),
      ('0' + this.day).slice(-2)
    ].join('-');
  }

  fromString(string: string): LocalDate {
    const parts = string.split('-');
    this.year = parseInt(parts[0]);
    this.month = parseInt(parts[1]) - 1;
    this.day = parseInt(parts[2]);
    return this;
  }
}
