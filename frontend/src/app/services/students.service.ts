import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  private apiUrl = '/api/students';

  constructor(private http: HttpClient) { }

getStudents(params: any): Observable<any> {
    let httpParams = new HttpParams();

    if (params.searchTerm) {
      httpParams = httpParams.append('searchTerm', params.searchTerm);
    }

    if (params.level) {
      httpParams = httpParams.append('level', params.level);
    }

    httpParams = httpParams.append('page', params.page);
    httpParams = httpParams.append('size', params.size);

    return this.http.get(this.apiUrl, { params: httpParams });
  }

  getStudent(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }

  createStudent(student: any): Observable<any> {
    return this.http.post(this.apiUrl, student);
  }

  updateStudent(id: number, student: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, student);
  }

  deleteStudent(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  exportStudents(): Observable<any> {
    return this.http.get(`${this.apiUrl}/export`, { responseType: 'blob' });
  }

  importStudents(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post(`${this.apiUrl}/import`, formData);
  }
}