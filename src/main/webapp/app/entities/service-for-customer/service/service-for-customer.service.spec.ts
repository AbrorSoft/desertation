import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IServiceForCustomer } from '../service-for-customer.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../service-for-customer.test-samples';

import { ServiceForCustomerService } from './service-for-customer.service';

const requireRestSample: IServiceForCustomer = {
  ...sampleWithRequiredData,
};

describe('ServiceForCustomer Service', () => {
  let service: ServiceForCustomerService;
  let httpMock: HttpTestingController;
  let expectedResult: IServiceForCustomer | IServiceForCustomer[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ServiceForCustomerService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ServiceForCustomer', () => {
      const serviceForCustomer = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(serviceForCustomer).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ServiceForCustomer', () => {
      const serviceForCustomer = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(serviceForCustomer).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ServiceForCustomer', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ServiceForCustomer', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ServiceForCustomer', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addServiceForCustomerToCollectionIfMissing', () => {
      it('should add a ServiceForCustomer to an empty array', () => {
        const serviceForCustomer: IServiceForCustomer = sampleWithRequiredData;
        expectedResult = service.addServiceForCustomerToCollectionIfMissing([], serviceForCustomer);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serviceForCustomer);
      });

      it('should not add a ServiceForCustomer to an array that contains it', () => {
        const serviceForCustomer: IServiceForCustomer = sampleWithRequiredData;
        const serviceForCustomerCollection: IServiceForCustomer[] = [
          {
            ...serviceForCustomer,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addServiceForCustomerToCollectionIfMissing(serviceForCustomerCollection, serviceForCustomer);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ServiceForCustomer to an array that doesn't contain it", () => {
        const serviceForCustomer: IServiceForCustomer = sampleWithRequiredData;
        const serviceForCustomerCollection: IServiceForCustomer[] = [sampleWithPartialData];
        expectedResult = service.addServiceForCustomerToCollectionIfMissing(serviceForCustomerCollection, serviceForCustomer);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceForCustomer);
      });

      it('should add only unique ServiceForCustomer to an array', () => {
        const serviceForCustomerArray: IServiceForCustomer[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const serviceForCustomerCollection: IServiceForCustomer[] = [sampleWithRequiredData];
        expectedResult = service.addServiceForCustomerToCollectionIfMissing(serviceForCustomerCollection, ...serviceForCustomerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const serviceForCustomer: IServiceForCustomer = sampleWithRequiredData;
        const serviceForCustomer2: IServiceForCustomer = sampleWithPartialData;
        expectedResult = service.addServiceForCustomerToCollectionIfMissing([], serviceForCustomer, serviceForCustomer2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceForCustomer);
        expect(expectedResult).toContain(serviceForCustomer2);
      });

      it('should accept null and undefined values', () => {
        const serviceForCustomer: IServiceForCustomer = sampleWithRequiredData;
        expectedResult = service.addServiceForCustomerToCollectionIfMissing([], null, serviceForCustomer, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serviceForCustomer);
      });

      it('should return initial array if no ServiceForCustomer is added', () => {
        const serviceForCustomerCollection: IServiceForCustomer[] = [sampleWithRequiredData];
        expectedResult = service.addServiceForCustomerToCollectionIfMissing(serviceForCustomerCollection, undefined, null);
        expect(expectedResult).toEqual(serviceForCustomerCollection);
      });
    });

    describe('compareServiceForCustomer', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareServiceForCustomer(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareServiceForCustomer(entity1, entity2);
        const compareResult2 = service.compareServiceForCustomer(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareServiceForCustomer(entity1, entity2);
        const compareResult2 = service.compareServiceForCustomer(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareServiceForCustomer(entity1, entity2);
        const compareResult2 = service.compareServiceForCustomer(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
