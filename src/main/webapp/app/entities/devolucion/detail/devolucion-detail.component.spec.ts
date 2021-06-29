import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DevolucionDetailComponent } from './devolucion-detail.component';

describe('Component Tests', () => {
  describe('Devolucion Management Detail Component', () => {
    let comp: DevolucionDetailComponent;
    let fixture: ComponentFixture<DevolucionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DevolucionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ devolucion: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DevolucionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DevolucionDetailComponent);
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
