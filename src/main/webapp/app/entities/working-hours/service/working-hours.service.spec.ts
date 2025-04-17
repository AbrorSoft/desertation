import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IWorkingHours } from '../working-hours.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../working-hours.test-samples';

import { WorkingHoursService, RestWorkingHours } from './working-hours.service';

const requireRestSample: RestWorkingHours = {
  ...sampleWithRequiredData,
  startTime: sampleWithRequiredData.startTime?.toJSON(),
  endTime: sampleWithRequiredData.endTime?.toJSON(),
};

describe('WorkingHours Service', () => {
  let service: WorkingHoursService;
  let httpMock: HttpTestingController;
  let expectedResult: IWorkingHours | IWorkingHours[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WorkingHoursService);
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

    it('should create a WorkingHours', () => {
      const workingHours = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(workingHours).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WorkingHours', () => {
      const workingHours = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(workingHours).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WorkingHours', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WorkingHours', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a WorkingHours', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addWorkingHoursToCollectionIfMissing', () => {
      it('should add a WorkingHours to an empty array', () => {
        const workingHours: IWorkingHours = sampleWithRequiredData;
        expectedResult = service.addWorkingHoursToCollectionIfMissing([], workingHours);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workingHours);
      });

      it('should not add a WorkingHours to an array that contains it', () => {
        const workingHours: IWorkingHours = sampleWithRequiredData;
        const workingHoursCollection: IWorkingHours[] = [
          {
            ...workingHours,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWorkingHoursToCollectionIfMissing(workingHoursCollection, workingHours);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WorkingHours to an array that doesn't contain it", () => {
        const workingHours: IWorkingHours = sampleWithRequiredData;
        const workingHoursCollection: IWorkingHours[] = [sampleWithPartialData];
        expectedResult = service.addWorkingHoursToCollectionIfMissing(workingHoursCollection, workingHours);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workingHours);
      });

      it('should add only unique WorkingHours to an array', () => {
        const workingHoursArray: IWorkingHours[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const workingHoursCollection: IWorkingHours[] = [sampleWithRequiredData];
        expectedResult = service.addWorkingHoursToCollectionIfMissing(workingHoursCollection, ...workingHoursArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const workingHours: IWorkingHours = sampleWithRequiredData;
        const workingHours2: IWorkingHours = sampleWithPartialData;
        expectedResult = service.addWorkingHoursToCollectionIfMissing([], workingHours, workingHours2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workingHours);
        expect(expectedResult).toContain(workingHours2);
      });

      it('should accept null and undefined values', () => {
        const workingHours: IWorkingHours = sampleWithRequiredData;
        expectedResult = service.addWorkingHoursToCollectionIfMissing([], null, workingHours, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workingHours);
      });

      it('should return initial array if no WorkingHours is added', () => {
        const workingHoursCollection: IWorkingHours[] = [sampleWithRequiredData];
        expectedResult = service.addWorkingHoursToCollectionIfMissing(workingHoursCollection, undefined, null);
        expect(expectedResult).toEqual(workingHoursCollection);
      });
    });

    describe('compareWorkingHours', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWorkingHours(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareWorkingHours(entity1, entity2);
        const compareResult2 = service.compareWorkingHours(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareWorkingHours(entity1, entity2);
        const compareResult2 = service.compareWorkingHours(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareWorkingHours(entity1, entity2);
        const compareResult2 = service.compareWorkingHours(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
