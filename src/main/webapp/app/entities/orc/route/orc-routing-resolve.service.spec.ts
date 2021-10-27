jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOrc, Orc } from '../orc.model';
import { OrcService } from '../service/orc.service';

import { OrcRoutingResolveService } from './orc-routing-resolve.service';

describe('Service Tests', () => {
  describe('Orc routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: OrcRoutingResolveService;
    let service: OrcService;
    let resultOrc: IOrc | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(OrcRoutingResolveService);
      service = TestBed.inject(OrcService);
      resultOrc = undefined;
    });

    describe('resolve', () => {
      it('should return IOrc returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrc = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOrc).toEqual({ id: 123 });
      });

      it('should return new IOrc if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrc = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultOrc).toEqual(new Orc());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrc = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOrc).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
