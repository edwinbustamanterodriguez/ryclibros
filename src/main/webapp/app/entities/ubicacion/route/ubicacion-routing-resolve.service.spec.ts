jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IUbicacion, Ubicacion } from '../ubicacion.model';
import { UbicacionService } from '../service/ubicacion.service';

import { UbicacionRoutingResolveService } from './ubicacion-routing-resolve.service';

describe('Service Tests', () => {
  describe('Ubicacion routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: UbicacionRoutingResolveService;
    let service: UbicacionService;
    let resultUbicacion: IUbicacion | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(UbicacionRoutingResolveService);
      service = TestBed.inject(UbicacionService);
      resultUbicacion = undefined;
    });

    describe('resolve', () => {
      it('should return IUbicacion returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUbicacion = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUbicacion).toEqual({ id: 123 });
      });

      it('should return new IUbicacion if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUbicacion = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultUbicacion).toEqual(new Ubicacion());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUbicacion = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUbicacion).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
