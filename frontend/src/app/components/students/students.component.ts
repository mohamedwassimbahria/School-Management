import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../services/auth.service';
import { StudentService } from 'src/app/services/students.service';

@Component({
  selector: 'app-students',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    MatInputModule,
    MatButtonModule,
    MatTableModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatSelectModule,
    MatIconModule
  ],
  templateUrl: './students.component.html',
  styleUrls: ['./students.component.css']
})
export class StudentsComponent implements OnInit {
  students: any[] = [];
  searchForm: FormGroup;
  studentForm: FormGroup;
  page = 0;
  size = 10;
  totalPages = 0;
  editing = false;
  displayedColumns: string[] = ['id', 'username', 'level', 'actions'];
  levels = ['FIRST_GRADE', 'SECOND_GRADE', 'THIRD_GRADE', 'FOURTH_GRADE', 'FIFTH_GRADE'];

  constructor(
    private studentService: StudentService,
    private authService: AuthService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.searchForm = this.fb.group({
      username: [''],
      level: ['']
    });
    this.studentForm = this.fb.group({
      id: [null],
      username: [''],
      level: ['']
    });
  }

  ngOnInit(): void {
    this.loadStudents();
  }

  loadStudents(): void {
    const params = {
      ...this.searchForm.value,
      page: this.page,
      size: this.size
    };
    this.studentService.getStudents(params).subscribe(response => {
      this.students = response.content;
      this.totalPages = response.totalPages;
    });
  }

  search(): void {
    this.page = 0;
    this.loadStudents();
  }

  edit(student: any): void {
    this.editing = true;
    this.studentForm.setValue(student);
  }

  save(): void {
    if (this.editing) {
      this.studentService.updateStudent(this.studentForm.value.id, this.studentForm.value).subscribe(() => {
        this.reset();
      });
    } else {
      this.studentService.createStudent(this.studentForm.value).subscribe(() => {
        this.reset();
      });
    }
  }

  delete(id: number): void {
    this.studentService.deleteStudent(id).subscribe(() => {
      this.loadStudents();
    });
  }

  reset(): void {
    this.editing = false;
    this.studentForm.reset();
    this.loadStudents();
  }

  nextPage(): void {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.loadStudents();
    }
  }

  prevPage(): void {
    if (this.page > 0) {
      this.page--;
      this.loadStudents();
    }
  }

  export(): void {
    this.studentService.exportStudents().subscribe(blob => {
      const a = document.createElement('a');
      const objectUrl = URL.createObjectURL(blob);
      a.href = objectUrl;
      a.download = 'students.csv';
      a.click();
      URL.revokeObjectURL(objectUrl);
    });
  }

  import(event: any): void {
    const file = event.target.files[0];
    this.studentService.importStudents(file).subscribe(() => {
      this.loadStudents();
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
