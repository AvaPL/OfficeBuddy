import {HttpHeaders} from "@angular/common/http";

export function requestHeaders(token: string): HttpHeaders {
  return new HttpHeaders({
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  });
}
