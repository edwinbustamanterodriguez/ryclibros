jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ProvinciaService } from '../service/provincia.service';

import { ProvinciaDeleteDialogComponent } from './provincia-delete-dialog.component';

describe('Component Tests', () => {
  describe('Provincia Management Delete Component', () => {
    let comp: ProvinciaDeleteDialogComponent;
    let fixture: ComponentFixture<ProvinciaDeleteDialogComponent>;
    let service: ProvinciaService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProvinciaDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(ProvinciaDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProvinciaDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ProvinciaService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
