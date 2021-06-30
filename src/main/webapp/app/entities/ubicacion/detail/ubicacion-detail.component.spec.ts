import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UbicacionDetailComponent } from './ubicacion-detail.component';

describe('Component Tests', () => {
  describe('Ubicacion Management Detail Component', () => {
    let comp: UbicacionDetailComponent;
    let fixture: ComponentFixture<UbicacionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [UbicacionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ ubicacion: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(UbicacionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UbicacionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ubicacion on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ubicacion).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
