jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILocalidad, Localidad } from '../localidad.model';
import { LocalidadService } from '../service/localidad.service';

import { LocalidadRoutingResolveService } from './localidad-routing-resolve.service';

describe('Service Tests', () => {
  describe('Localidad routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LocalidadRoutingResolveService;
    let service: LocalidadService;
    let resultLocalidad: ILocalidad | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LocalidadRoutingResolveService);
      service = TestBed.inject(LocalidadService);
      resultLocalidad = undefined;
    });

    describe('resolve', () => {
      it('should return ILocalidad returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLocalidad = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLocalidad).toEqual({ id: 123 });
      });

      it('should return new ILocalidad if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLocalidad = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLocalidad).toEqual(new Localidad());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLocalidad = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLocalidad).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
