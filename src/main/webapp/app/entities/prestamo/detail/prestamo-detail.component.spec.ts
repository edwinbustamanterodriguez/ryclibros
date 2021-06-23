import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrestamoDetailComponent } from './prestamo-detail.component';

describe('Component Tests', () => {
  describe('Prestamo Management Detail Component', () => {
    let comp: PrestamoDetailComponent;
    let fixture: ComponentFixture<PrestamoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PrestamoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ prestamo: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PrestamoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PrestamoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load prestamo on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.prestamo).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
