import { create } from "zustand";

type StatusState = {
  generationStatus?: string;
  setGenerationStatus: (status?: string) => void;
};

const useGenerationStatus = create<StatusState>((set) => ({
  generationStatus: undefined,
  setGenerationStatus: (status) => set({ generationStatus: status }),
}));

export default useGenerationStatus;
