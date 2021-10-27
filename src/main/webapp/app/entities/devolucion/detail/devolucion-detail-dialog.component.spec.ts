import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DevolucionDetailDialogComponent } from './devolucion-detail-dialog.component';

describe('Component Tests', () => {
  describe('Devolucion Management Detail Component', () => {
    let comp: DevolucionDetailDialogComponent;
    let fixture: ComponentFixture<DevolucionDetailDialogComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DevolucionDetailDialogComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ devolucion: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DevolucionDetailDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DevolucionDetailDialogComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load devolucion on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.devolucion).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
