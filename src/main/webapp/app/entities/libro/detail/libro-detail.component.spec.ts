import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LibroDetailComponent } from './libro-detail.component';

describe('Component Tests', () => {
  describe('Libro Management Detail Component', () => {
    let comp: LibroDetailComponent;
    let fixture: ComponentFixture<LibroDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LibroDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ libro: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LibroDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LibroDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load libro on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.libro).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
