import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrcDetailComponent } from './orc-detail.component';

describe('Component Tests', () => {
  describe('Orc Management Detail Component', () => {
    let comp: OrcDetailComponent;
    let fixture: ComponentFixture<OrcDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [OrcDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ orc: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(OrcDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OrcDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load orc on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.orc).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
