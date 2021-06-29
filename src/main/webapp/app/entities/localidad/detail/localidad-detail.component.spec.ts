import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LocalidadDetailComponent } from './localidad-detail.component';

describe('Component Tests', () => {
  describe('Localidad Management Detail Component', () => {
    let comp: LocalidadDetailComponent;
    let fixture: ComponentFixture<LocalidadDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LocalidadDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ localidad: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LocalidadDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LocalidadDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load localidad on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.localidad).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
