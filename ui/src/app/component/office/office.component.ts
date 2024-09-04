import {Component, ElementRef, inject, OnInit, ViewChild} from '@angular/core';
import {PageEvent} from "@angular/material/paginator";
import {MatDialog} from "@angular/material/dialog";
import {DeleteOfficeDialogComponent} from "./delete-office-dialog/delete-office-dialog.component";
import {CreateOfficeDialogComponent} from "./create-office-dialog/create-office-dialog.component";
import {
  EditOfficeDialogComponent,
  EditOfficeInitialValuesDialogData
} from "./edit-office-dialog/edit-office-dialog.component";
import {OfficeView} from "../../service/model/office/office-view.model";
import {Pagination} from "../../service/model/pagination/pagination.model";
import {OfficeService} from "../../service/office.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-office',
  templateUrl: './office.component.html',
  styleUrl: './office.component.scss'
})
export class OfficeComponent implements OnInit {

  @ViewChild('officeCards') officeCards!: ElementRef;

  readonly createOfficeDialog = inject(MatDialog);
  readonly editOfficeDialog = inject(MatDialog);
  readonly deleteOfficeDialog = inject(MatDialog);

  offices: OfficeView[] = [];
  pagination: Pagination = {
    limit: 10,
    offset: 0,
    hasMoreResults: false
  };

  constructor(private officeService: OfficeService) {
  }

  ngOnInit() {
    this.fetchOffices(this.pagination.limit, this.pagination.offset);
  }

  async fetchOffices(limit: number, offset: number) {
    let response = await this.officeService.getOfficeListView(limit, offset);
    this.offices = response.offices;
    this.pagination = response.pagination;
    this.officeCards.nativeElement.scrollIntoView(true);
  }

  editManagers(officeId: string) {
    console.log(`Editing office managers of office ${officeId}`);
  }

  createOffice() {
    const dialogRef = this.createOfficeDialog.open(CreateOfficeDialogComponent);

    dialogRef.afterClosed().subscribe(createdOffice => {
      if (createdOffice) {
        this.fetchOffices(this.pagination.limit, 0)
      }
    });
  }

  editOffice(editOfficeInitialValues: EditOfficeInitialValuesDialogData) {
    const dialogRef = this.editOfficeDialog.open(EditOfficeDialogComponent, {
      data: editOfficeInitialValues
    })

    dialogRef.afterClosed().subscribe(editedOffice => {
      if (editedOffice) {
        this.fetchOffices(this.pagination.limit, 0)
      }
    });
  }

  deleteOffice(officeId: string, officeName: string) {
    const dialogRef = this.deleteOfficeDialog.open(DeleteOfficeDialogComponent, {data: {officeId, officeName}});

    dialogRef.afterClosed().subscribe(isConfirmed => {
      if (isConfirmed) {
        this.fetchOffices(this.pagination.limit, 0)
      }
    });
  }

  // TODO: Scroll to top on page change
  handlePageEvent(event: PageEvent) {
    this.fetchOffices(this.pagination.limit, event.pageIndex * this.pagination.limit)
  }
}
