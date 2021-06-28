jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { PrestamoService } from '../service/prestamo.service';

import { PrestamoDevolverDialogComponent } from './prestamo-devolver-dialog.component';

describe('Component Tests', () => {
  describe('Prestamo Management Delete Component', () => {
    let comp: PrestamoDevolverDialogComponent;
    let fixture: ComponentFixture<PrestamoDevolverDialogComponent>;
    let service: PrestamoService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PrestamoDevolverDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(PrestamoDevolverDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PrestamoDevolverDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PrestamoService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          // comp.confirmDevolucion(123);
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
